package com.example.libraryapp.domain.model

enum class SortOrder { RELEVANCE, NEWEST, OLDEST }

data class SearchFilters(
    val genre: String? = null,
    val sort: SortOrder = SortOrder.RELEVANCE
) {
    val isActive: Boolean get() = this != SearchFilters()
}