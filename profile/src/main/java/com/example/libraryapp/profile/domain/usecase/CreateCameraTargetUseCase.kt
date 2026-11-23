package com.example.libraryapp.profile.domain.usecase

import com.example.libraryapp.profile.domain.repository.PhotoStorage

class CreateCameraTargetUseCase(private val storage: PhotoStorage) {
    operator fun invoke(): String = storage.createCameraTarget()
}