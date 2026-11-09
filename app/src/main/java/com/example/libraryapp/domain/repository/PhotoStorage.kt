package com.example.libraryapp.domain.repository

interface PhotoStorage {
    fun createCameraTarget(): String
    suspend fun importPhoto(sourceUri: String): String
}