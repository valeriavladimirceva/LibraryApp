package com.example.libraryapp.di

import android.content.Context
import com.example.libraryapp.data.local.DataStoreFiltersRepository
import com.example.libraryapp.data.local.DataStoreProfileRepository
import com.example.libraryapp.data.local.FilePhotoStorage
import com.example.libraryapp.data.local.db.AppDatabase
import com.example.libraryapp.data.remote.OpenLibraryApi
import com.example.libraryapp.data.repository.BookRepositoryImpl
import com.example.libraryapp.data.repository.FavoritesRepositoryImpl
import com.example.libraryapp.data.repository.OkHttpResumeRepository
import com.example.libraryapp.domain.cache.FiltersBadgeCache
import com.example.libraryapp.domain.repository.BookRepository
import com.example.libraryapp.domain.repository.FavoritesRepository
import com.example.libraryapp.domain.repository.FiltersRepository
import com.example.libraryapp.domain.repository.PhotoStorage
import com.example.libraryapp.domain.repository.ProfileRepository
import com.example.libraryapp.domain.repository.ResumeRepository
import com.example.libraryapp.domain.usecase.CreateCameraTargetUseCase
import com.example.libraryapp.domain.usecase.DownloadResumeUseCase
import com.example.libraryapp.domain.usecase.GetBookDetailsUseCase
import com.example.libraryapp.domain.usecase.ImportPhotoUseCase
import com.example.libraryapp.domain.usecase.ObserveProfileUseCase
import com.example.libraryapp.domain.usecase.SaveProfileUseCase
import com.example.libraryapp.domain.usecase.SearchBooksUseCase
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class AppContainer(context: Context) {
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
    val filtersRepository: FiltersRepository = DataStoreFiltersRepository(context)
    val filtersBadgeCache = FiltersBadgeCache()

    private val database = AppDatabase.create(context)
    val favoritesRepository: FavoritesRepository = FavoritesRepositoryImpl(database.favoriteDao())

    val profileRepository: ProfileRepository = DataStoreProfileRepository(context)
    private val photoStorage: PhotoStorage = FilePhotoStorage(context)
    private val resumeRepository: ResumeRepository = OkHttpResumeRepository(context, client)

    val observeProfile = ObserveProfileUseCase(profileRepository)
    val saveProfile = SaveProfileUseCase(profileRepository)
    val createCameraTarget = CreateCameraTargetUseCase(photoStorage)
    val importPhoto = ImportPhotoUseCase(photoStorage)
    val downloadResume = DownloadResumeUseCase(resumeRepository)
}

