package com.example.libraryapp.domain.repository

import com.example.libraryapp.domain.model.Book
import com.example.libraryapp.domain.model.BooksPage

interface BookRepository {
    suspend fun searchBooks(query: String, page: Int, limit: Int): Result<BooksPage>
    suspend fun getBookDetails(id: String): Result<Book>
}