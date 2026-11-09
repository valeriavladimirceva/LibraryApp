package com.example.libraryapp.domain.usecase

import com.example.libraryapp.domain.repository.PhotoStorage

class ImportPhotoUseCase(private val storage: PhotoStorage) {
    suspend operator fun invoke(sourceUri: String): String = storage.importPhoto(sourceUri)
}