package com.example.libraryapp.profile.domain.usecase

import com.example.libraryapp.profile.domain.repository.PhotoStorage

class ImportPhotoUseCase(private val storage: PhotoStorage) {
    suspend operator fun invoke(sourceUri: String): String = storage.importPhoto(sourceUri)
}