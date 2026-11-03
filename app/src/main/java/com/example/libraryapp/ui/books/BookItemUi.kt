package com.example.libraryapp.ui.books

import com.example.libraryapp.domain.model.Book

data class BookItemUi(
    val id: String,
    val title: String,
    val authors: String,
    val year: String,
    val coverId: Int?
)

fun Book.toItemUi() = BookItemUi(
    id = id,
    title = title,
    authors = authors.joinToString(", "),
    year = firstPublishYear?.toString() ?: "—",
    coverId = coverId
)