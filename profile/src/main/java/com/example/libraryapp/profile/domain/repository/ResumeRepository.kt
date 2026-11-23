package com.example.libraryapp.profile.domain.repository

interface ResumeRepository {
    suspend fun download(url: String): Result<String>
}