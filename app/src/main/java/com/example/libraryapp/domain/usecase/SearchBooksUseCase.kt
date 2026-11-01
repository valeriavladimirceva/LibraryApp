package com.example.libraryapp.domain.usecase

import com.example.libraryapp.domain.model.BooksPage
import com.example.libraryapp.domain.model.SearchFilters
import com.example.libraryapp.domain.repository.BookRepository

class SearchBooksUseCase(private val repository: BookRepository) {
    suspend operator fun invoke(
        query: String,
        page: Int, filters:
        SearchFilters = SearchFilters(),
        limit: Int = 20
    ): Result<BooksPage> {
        val q = query.trim()
        val hasFieldFilter = filters.genre != null
        return when {
            q.isEmpty() && !hasFieldFilter -> repository.searchBooks(DEFAULT_QUERY, page, limit, filters)
            q.isNotEmpty() && q.length < MIN_QUERY_LENGTH && !hasFieldFilter -> Result.success(BooksPage(emptyList(), 0))
            else -> repository.searchBooks(q, page, limit, filters)
        }
    }

    private companion object {
        const val DEFAULT_QUERY = "subject:fantasy"
        const val MIN_QUERY_LENGTH = 3
    }
}