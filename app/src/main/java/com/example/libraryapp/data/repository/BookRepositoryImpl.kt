package com.example.libraryapp.data.repository

import com.example.libraryapp.data.remote.OpenLibraryApi
import com.example.libraryapp.data.remote.descriptionText
import com.example.libraryapp.data.remote.toDomain
import com.example.libraryapp.domain.model.Book
import com.example.libraryapp.domain.model.BooksPage
import com.example.libraryapp.domain.model.DomainError
import com.example.libraryapp.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.cancellation.CancellationException

class BookRepositoryImpl(private val api: OpenLibraryApi): BookRepository {

    private val cache = ConcurrentHashMap<String, Book>()

    override suspend fun searchBooks(query: String, page: Int, limit: Int) = safeCall {
        val response = api.search(query, page, limit)
        val books = response.docs.map { it.toDomain() }
        books.forEach { cache[it.id] = it }
        BooksPage(books, response.numFound)
    }

    override suspend fun getBookDetails(id: String) = safeCall {
        val work = api.getWork(id)
        val base = cache[id] ?: Book(
            id = id, title = work.title, authors = emptyList(),
            coverId = work.covers.firstOrNull(), firstPublishYear = null,
            publishers = emptyList(), isbn = null, description = "", subjects = emptyList()
        )
        base.copy(description = work.descriptionText(), subjects = work.subjects.take(10))
    }

    private suspend fun <T> safeCall(block: suspend () -> T): Result<T> =
        withContext(Dispatchers.IO) {
            try {
                Result.success(block())
            } catch (e: CancellationException) {
                throw e
            } catch (e: HttpException) {
                Result.failure(if (e.code() == 404) DomainError.NotFound else DomainError.Server)
            } catch (e: IOException) {
                Result.failure(DomainError.NoConnection)
            } catch (e: Exception) {
                Result.failure(DomainError.Unknown)
            }
        }
}