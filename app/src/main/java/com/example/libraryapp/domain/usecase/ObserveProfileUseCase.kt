package com.example.libraryapp.domain.usecase

import com.example.libraryapp.domain.model.Profile
import com.example.libraryapp.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class ObserveProfileUseCase(private val repository: ProfileRepository) {
    operator fun invoke(): Flow<Profile> = repository.profile
}