package com.example.libraryapp.data.repository

import com.example.libraryapp.data.local.db.FavoriteDao
import com.example.libraryapp.data.local.db.toDomain
import com.example.libraryapp.data.local.db.toEntity
import com.example.libraryapp.domain.model.Book
import com.example.libraryapp.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(private val dao: FavoriteDao) : FavoritesRepository {

    override val favorites: Flow<List<Book>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun isFavorite(id: String): Flow<Boolean> = dao.observeIsFavorite(id)

    override suspend fun getFavorite(id: String): Book? = dao.getById(id)?.toDomain()

    override suspend fun add(book: Book) = dao.upsert(book.toEntity())

    override suspend fun remove(id: String) = dao.delete(id)
}