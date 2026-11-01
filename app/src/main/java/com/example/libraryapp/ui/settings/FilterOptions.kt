package com.example.libraryapp.ui.settings

import androidx.annotation.StringRes
import com.example.libraryapp.R
import com.example.libraryapp.domain.model.SortOrder

class FilterOption<out T>(val value: T, @StringRes val label: Int)

object FilterOptions {
    val genres: List<FilterOption<String?>> = listOf(
        FilterOption(null, R.string.filter_any),
        FilterOption("fantasy", R.string.genre_fantasy),
        FilterOption("science fiction", R.string.genre_scifi),
        FilterOption("mystery", R.string.genre_mystery),
        FilterOption("romance", R.string.genre_romance),
        FilterOption("history", R.string.genre_history),
        FilterOption("horror", R.string.genre_horror)
    )


    val sorts: List<FilterOption<SortOrder>> = listOf(
        FilterOption(SortOrder.RELEVANCE, R.string.sort_relevance),
        FilterOption(SortOrder.NEWEST, R.string.sort_newest),
        FilterOption(SortOrder.OLDEST, R.string.sort_oldest)
    )
}