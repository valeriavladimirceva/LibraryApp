package com.example.libraryapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.libraryapp.di.LibraryApplication
import com.example.libraryapp.domain.model.Profile
import com.example.libraryapp.domain.usecase.CreateCameraTargetUseCase
import com.example.libraryapp.domain.usecase.ImportPhotoUseCase
import com.example.libraryapp.domain.usecase.ObserveProfileUseCase
import com.example.libraryapp.domain.usecase.SaveProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditProfileUiState(
    val fullName: String = "",
    val photoUri: String? = null,
    val resumeUrl: String = "",
    val isLoaded: Boolean = false
)

class EditProfileViewModel(
    private val observeProfile: ObserveProfileUseCase,
    private val saveProfile: SaveProfileUseCase,
    private val importPhoto: ImportPhotoUseCase,
    private val createCameraTarget: CreateCameraTargetUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private var pendingCameraUri: String? = null

    init {
        viewModelScope.launch {
            val saved = observeProfile().first()
            _uiState.value = EditProfileUiState(
                fullName = saved.fullName,
                photoUri = saved.photoUri,
                resumeUrl = saved.resumeUrl,
                isLoaded = true
            )
        }
    }

    fun onFullNameChange(value: String) = _uiState.update { it.copy(fullName = value) }
    fun onResumeUrlChange(value: String) = _uiState.update { it.copy(resumeUrl = value) }

    fun onPhotoPicked(sourceUri: String) {
        viewModelScope.launch {
            runCatching { importPhoto(sourceUri) }
                .onSuccess { copied -> _uiState.update { it.copy(photoUri = copied) } }
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

    fun onDone(onSaved: () -> Unit) {
        val s = _uiState.value
        viewModelScope.launch {
            saveProfile(
                Profile(
                    fullName = s.fullName.trim(),
                    photoUri = s.photoUri,
                    resumeUrl = s.resumeUrl.trim()
                )
            )
            onSaved()
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LibraryApplication
                EditProfileViewModel(
                    app.container.observeProfile,
                    app.container.saveProfile,
                    app.container.importPhoto,
                    app.container.createCameraTarget
                )
            }
        }
    }
}