package com.example.libraryapp.domain.repository

interface ResumeRepository {
    suspend fun download(url: String): Result<String>
}