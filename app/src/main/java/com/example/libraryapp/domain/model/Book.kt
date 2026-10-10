package com.example.libraryapp.domain.model

data class Book(
    val id: String,
    val title: String,
    val authors: List<String>,
    val coverId: Int?,
    val firstPublishYear: Int?,
    val publishers: List<String>,
    val isbn: String?,
    val description: String,
    val subjects: List<String>
)