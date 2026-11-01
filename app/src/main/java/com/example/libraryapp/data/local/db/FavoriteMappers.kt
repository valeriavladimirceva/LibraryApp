package com.example.libraryapp.data.local.db

import com.example.libraryapp.domain.model.Book

fun Book.toEntity(addedAt: Long = System.currentTimeMillis()) = FavoriteEntity(
    id = id,
    title = title,
    authors = authors,
    coverId = coverId,
    firstPublishYear = firstPublishYear,
    publishers = publishers,
    isbn = isbn,
    description = description,
    subjects = subjects,
    addedAt = addedAt
)

fun FavoriteEntity.toDomain() = Book(
    id = id,
    title = title,
    authors = authors,
    coverId = coverId,
    firstPublishYear = firstPublishYear,
    publishers = publishers,
    isbn = isbn,
    description = description,
    subjects = subjects
)