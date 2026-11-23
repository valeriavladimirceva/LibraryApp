package com.example.libraryapp.profile.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.libraryapp.profile.di.ProfileContainerProvider
import com.example.libraryapp.profile.domain.model.LessonTime
import com.example.libraryapp.profile.domain.model.LessonTimeValidation
import com.example.libraryapp.profile.domain.model.Profile
import com.example.libraryapp.profile.domain.repository.ScheduleResult
import com.example.libraryapp.profile.domain.usecase.CreateCameraTargetUseCase
import com.example.libraryapp.profile.domain.usecase.ImportPhotoUseCase
import com.example.libraryapp.profile.domain.usecase.ObserveProfileUseCase
import com.example.libraryapp.profile.domain.usecase.SaveProfileUseCase
import com.example.libraryapp.profile.domain.usecase.ScheduleReminderUseCase
import com.example.libraryapp.profile.domain.usecase.ValidateLessonTimeUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

data class EditProfileUiState(
    val fullName: String = "",
    val photoUri: String? = null,
    val resumeUrl: String = "",
    val lessonTimeText: String = "",
    val lessonTimeValidation: LessonTimeValidation = LessonTimeValidation.Empty,
    val lessonTimeTouched: Boolean = false,
    val isLoaded: Boolean = false,
) {
    val canSave: Boolean
        get() = isLoaded && lessonTimeValidation is LessonTimeValidation.Valid
}

enum class DoneResult { Scheduled, NeedExactAlarm, NotificationsDenied }

enum class EditProfileEvent { PhotoImportFailed }

class EditProfileViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val observeProfile: ObserveProfileUseCase,
    private val saveProfile: SaveProfileUseCase,
    private val importPhoto: ImportPhotoUseCase,
    private val createCameraTarget: CreateCameraTargetUseCase,
    private val validateLessonTime: ValidateLessonTimeUseCase,
    private val scheduleReminder: ScheduleReminderUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _events = Channel<EditProfileEvent>(Channel.BUFFERED)
    val events: Flow<EditProfileEvent> = _events.receiveAsFlow()

    private var pendingCameraUri: String?
        get() = savedStateHandle[KEY_PENDING_CAMERA_URI]
        set(value) {
            savedStateHandle[KEY_PENDING_CAMERA_URI] = value
        }

    init {
        viewModelScope.launch {
            val saved = observeProfile().first()
            val timeText = saved.lessonTime?.toString().orEmpty()
            _uiState.update {
                it.copy(
                    fullName = saved.fullName,
                    photoUri = it.photoUri ?: saved.photoUri,
                    resumeUrl = saved.resumeUrl,
                    lessonTimeText = timeText,
                    lessonTimeValidation = validateLessonTime(timeText),
                    isLoaded = true,
                )
            }
        }
    }

    fun onFullNameChange(value: String) = _uiState.update { it.copy(fullName = value) }
    fun onResumeUrlChange(value: String) = _uiState.update { it.copy(resumeUrl = value) }

    fun onLessonTimeChange(value: String) = _uiState.update {
        it.copy(
            lessonTimeText = value,
            lessonTimeValidation = validateLessonTime(value),
            lessonTimeTouched = true
        )
    }

    fun onLessonTimePicked(hour: Int, minute: Int) =
        onLessonTimeChange(LessonTime(hour, minute).toString())

    fun onPhotoPicked(sourceUri: String) {
        viewModelScope.launch {
            try {
                val copied = importPhoto(sourceUri)
                _uiState.update { it.copy(photoUri = copied) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _events.send(EditProfileEvent.PhotoImportFailed)
            }
        }
    }

    fun prepareCameraTarget(): String {
        val target = createCameraTarget()
        pendingCameraUri = target
        return target
    }

    fun onCameraResult(success: Boolean) {
        val target = pendingCameraUri
        pendingCameraUri = null
        if (success && target != null) {
            _uiState.update { it.copy(photoUri = target) }
        }
    }

    fun onDone(notificationsAllowed: Boolean, onResult: (DoneResult) -> Unit) {
        val s = _uiState.value
        val valid = s.lessonTimeValidation as? LessonTimeValidation.Valid ?: return
        viewModelScope.launch {
            saveProfile(
                Profile(
                    fullName = s.fullName.trim(),
                    photoUri = s.photoUri,
                    resumeUrl = s.resumeUrl.trim(),
                    lessonTime = valid.time
                )
            )
            val result = if (!notificationsAllowed) {
                DoneResult.NotificationsDenied
            } else {
                when (scheduleReminder(valid.time, s.fullName)) {
                    ScheduleResult.Scheduled -> DoneResult.Scheduled
                    ScheduleResult.ExactAlarmNotAllowed -> DoneResult.NeedExactAlarm
                }
            }
            onResult(result)
        }
    }

    companion object {
        private const val KEY_PENDING_CAMERA_URI = "pending_camera_uri"

        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                val container = (app as ProfileContainerProvider).profileContainer
                EditProfileViewModel(
                    createSavedStateHandle(),
                    container.observeProfile,
                    container.saveProfile,
                    container.importPhoto,
                    container.createCameraTarget,
                    container.validateLessonTime,
                    container.scheduleReminder
                )
            }
        }
    }
}