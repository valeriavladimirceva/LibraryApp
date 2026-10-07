package com.example.libraryapp.ui.navigation

sealed class Screen(val route: String) {
    data object Books: Screen("books")
    data object Favorites: Screen("favorites")
    data object Profile: Screen("profile")

    data object BookDetails: Screen("book_detail/{bookId}") {
        const val ARG_BOOK_ID = "bookId"
        fun createRoute(bookId: String): String = "book_detail/${bookId}"
    }
}