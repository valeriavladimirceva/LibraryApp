package com.example.libraryapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.libraryapp.di.LibraryApplication
import com.example.libraryapp.domain.model.DomainError
import com.example.libraryapp.domain.usecase.DownloadResumeUseCase
import com.example.libraryapp.domain.usecase.ObserveProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ResumeState {
    data object Idle : ResumeState
    data object Loading : ResumeState
    data class Ready(val uri: String) : ResumeState
    data class Error(val error: DomainError) : ResumeState
}

data class ProfileUiState(
    val profile: ProfileUi = ProfileUi(),
    val resume: ResumeState = ResumeState.Idle
)

class ProfileViewModel(
    private val observeProfile: ObserveProfileUseCase,
    private val downloadResume: DownloadResumeUseCase
) : ViewModel() {

    private val resume = MutableStateFlow<ResumeState>(ResumeState.Idle)

    val uiState: StateFlow<ProfileUiState> =
        combine(observeProfile(), resume) { profile, resumeState ->
            ProfileUiState(profile.toUi(), resumeState)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProfileUiState())

    fun onResumeClick() {
        if (resume.value is ResumeState.Loading) return
        viewModelScope.launch {
            val url = observeProfile().first().resumeUrl
            resume.value = ResumeState.Loading
            downloadResume(url)
                .onSuccess { resume.value = ResumeState.Ready(it) }
                .onFailure { e ->
                    resume.value = ResumeState.Error(e as? DomainError ?: DomainError.Unknown)
                }
        }
    }

    fun onResumeHandled() {
        resume.value = ResumeState.Idle
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LibraryApplication
                ProfileViewModel(
                    app.container.observeProfile,
                    app.container.downloadResume
                )
            }
        }
    }
}