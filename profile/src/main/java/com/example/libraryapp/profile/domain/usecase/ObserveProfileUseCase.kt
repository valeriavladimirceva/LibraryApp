package com.example.libraryapp.profile.domain.usecase

import com.example.libraryapp.profile.domain.model.Profile
import com.example.libraryapp.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class ObserveProfileUseCase(private val repository: ProfileRepository) {
    operator fun invoke(): Flow<Profile> = repository.profile
}