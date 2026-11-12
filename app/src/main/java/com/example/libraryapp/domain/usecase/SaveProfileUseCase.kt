package com.example.libraryapp.domain.usecase

import com.example.libraryapp.domain.model.Profile
import com.example.libraryapp.domain.repository.ProfileRepository

class SaveProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(profile: Profile) = repository.save(profile)
}