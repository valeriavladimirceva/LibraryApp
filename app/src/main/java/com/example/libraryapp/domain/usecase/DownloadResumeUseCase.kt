package com.example.libraryapp.domain.usecase

import com.example.libraryapp.domain.repository.ResumeRepository

class DownloadResumeUseCase(private val repository: ResumeRepository) {
    suspend operator fun invoke(url: String): Result<String> = repository.download(url.trim())
}