package com.example.libraryapp.profile.domain.repository

interface PhotoStorage {
    fun createCameraTarget(): String
    suspend fun importPhoto(sourceUri: String): String
}