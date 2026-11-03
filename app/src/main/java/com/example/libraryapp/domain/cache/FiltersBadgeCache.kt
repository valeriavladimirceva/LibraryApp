package com.example.libraryapp.domain.cache

import com.example.libraryapp.domain.model.SearchFilters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FiltersBadgeCache {
    private val _hasActiveFilters = MutableStateFlow(false)
    val hasActiveFilters: StateFlow<Boolean> = _hasActiveFilters.asStateFlow()

    fun update(filters: SearchFilters) {
        _hasActiveFilters.value = filters.isActive
    }
}