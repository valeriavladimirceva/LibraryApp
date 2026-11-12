package com.example.libraryapp.domain.usecase

import com.example.libraryapp.domain.repository.PhotoStorage

class CreateCameraTargetUseCase(private val storage: PhotoStorage) {
    operator fun invoke(): String = storage.createCameraTarget()
}