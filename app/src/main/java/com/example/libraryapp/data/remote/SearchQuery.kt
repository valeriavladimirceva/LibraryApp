package com.example.libraryapp.data.remote

import com.example.libraryapp.domain.model.SearchFilters
import com.example.libraryapp.domain.model.SortOrder

internal fun buildSearchQuery(text: String, filters: SearchFilters): String =
    listOfNotNull(
        text.trim().takeIf { it.isNotEmpty() },
        filters.genre?.let { "subject:\"$it\"" },
    ).joinToString(" ")

internal fun SortOrder.toApiValue(): String? = when (this) {
    SortOrder.RELEVANCE -> null
    SortOrder.NEWEST -> "new"
    SortOrder.OLDEST -> "old"
}