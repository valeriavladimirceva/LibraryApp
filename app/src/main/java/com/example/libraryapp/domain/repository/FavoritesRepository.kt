package com.example.libraryapp.domain.repository

import com.example.libraryapp.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    val favorites: Flow<List<Book>>
    fun isFavorite(id: String): Flow<Boolean>
    suspend fun getFavorite(id: String): Book?
    suspend fun add(book: Book)
    suspend fun remove(id: String)
}
