package com.example.libraryapp.domain.usecase

import com.example.libraryapp.domain.repository.BookRepository

class GetBookDetailsUseCase(private val repository: BookRepository) {
    suspend operator fun invoke(id: String) = repository.getBookDetails(id)
}