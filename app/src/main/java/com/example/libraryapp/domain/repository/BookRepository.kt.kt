package com.example.libraryapp.domain.repository

import com.example.libraryapp.domain.model.Book
import com.example.libraryapp.domain.model.BooksPage
import com.example.libraryapp.domain.model.SearchFilters

interface BookRepository {
    suspend fun searchBooks(query: String, page: Int, limit: Int, filters: SearchFilters): Result<BooksPage>
    suspend fun getBookDetails(id: String): Result<Book>
}