package com.example.libraryapp.data.remote

import com.example.libraryapp.data.remote.dto.SearchDocDto
import com.example.libraryapp.data.remote.dto.WorkDto
import com.example.libraryapp.domain.model.Book
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

fun SearchDocDto.toDomain(): Book = Book(
    id = key.removePrefix("/works/"),
    title = title,
    authors = authorNames,
    coverId = coverId,
    firstPublishYear = firstPublishYear,
    publishers = publisher.take(3),
    isbn = isbn.firstOrNull(),
    description = "",
    subjects = emptyList()
)

fun WorkDto.descriptionText(): String = when (val d = description) {
    null, is JsonNull -> ""
    is JsonPrimitive -> d.content
    is JsonObject -> d["value"]?.jsonPrimitive?.content.orEmpty()
    else -> ""
}