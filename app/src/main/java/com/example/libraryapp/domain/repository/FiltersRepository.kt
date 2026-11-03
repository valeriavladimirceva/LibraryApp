package com.example.libraryapp.domain.repository

import com.example.libraryapp.domain.model.SearchFilters
import kotlinx.coroutines.flow.Flow

interface FiltersRepository {
    val filters: Flow<SearchFilters>
    suspend fun saveFilters(filters: SearchFilters)
}