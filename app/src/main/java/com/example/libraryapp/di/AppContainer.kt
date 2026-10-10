package com.example.libraryapp.di

import com.example.libraryapp.data.remote.OpenLibraryApi
import com.example.libraryapp.data.repository.BookRepositoryImpl
import com.example.libraryapp.domain.repository.BookRepository
import com.example.libraryapp.domain.usecase.GetBookDetailsUseCase
import com.example.libraryapp.domain.usecase.SearchBooksUseCase
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

class AppContainer {
    private val json = Json { ignoreUnknownKeys = true }

    private val client = OkHttpClient.Builder()
        .addInterceptor {
            chain ->
            chain.proceed(chain.request().newBuilder()
                .header("User-Agent", "LibraryApp (student project)")
                .build()
            )
        }
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    private val api: OpenLibraryApi = Retrofit.Builder()
        .baseUrl("https://openlibrary.org/")
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(OpenLibraryApi::class.java)

    private val repository: BookRepository = BookRepositoryImpl(api)

    val searchBooks = SearchBooksUseCase(repository)
    val getBookDetails = GetBookDetailsUseCase(repository)
}

