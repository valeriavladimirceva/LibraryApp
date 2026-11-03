package com.example.libraryapp.ui.details

import com.example.libraryapp.domain.model.Book

data class BookDetailsUi(
    val title: String,
    val authors: String,
    val coverId: Int?,
    val firstPublished: String,
    val publishers: String,
    val isbn: String,
    val description: String,
    val subjects: List<String>
)

private const val EMPTY = "—"

fun Book.toDetailsUi() = BookDetailsUi(
    title = title,
    authors = authors.joinToString(", "),
    coverId = coverId,
    firstPublished = firstPublishYear?.toString() ?: EMPTY,
    publishers = publishers.joinToString(", ").ifBlank { EMPTY },
    isbn = isbn ?: EMPTY,
    description = description,
    subjects = subjects
)