package com.example.libraryapp.domain.repository

import com.example.libraryapp.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profile: Flow<Profile>
    suspend fun save(profile: Profile)
}