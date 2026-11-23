package com.example.libraryapp.profile.domain.usecase

import com.example.libraryapp.profile.domain.model.Profile
import com.example.libraryapp.profile.domain.repository.ProfileRepository

class SaveProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(profile: Profile) = repository.save(profile)
}