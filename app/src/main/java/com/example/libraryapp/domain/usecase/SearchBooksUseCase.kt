package com.example.libraryapp.domain.usecase

import com.example.libraryapp.domain.model.BooksPage
import com.example.libraryapp.domain.repository.BookRepository

class SearchBooksUseCase(private val repository: BookRepository) {
    suspend operator fun invoke(query: String, page: Int, limit: Int = 20): Result<BooksPage> {
        val q = query.trim()
        return when {
            q.isEmpty() -> repository.searchBooks(DEFAULT_QUERY, page, limit)
            q.length < MIN_QUERY_LENGTH -> Result.success(BooksPage(emptyList(), 0))
            else -> repository.searchBooks(q, page, limit)
        }
    }

    private companion object {
        const val DEFAULT_QUERY = "subject:fantasy"
        const val MIN_QUERY_LENGTH = 3
    }
}