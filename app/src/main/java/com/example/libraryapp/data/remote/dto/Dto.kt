package com.example.libraryapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class WorkDto(
    val title: String = "",
    val description: JsonElement? = null,
    val subjects: List<String> = emptyList(),
    val covers: List<Int> = emptyList()

)

@Serializable
data class SearchDocDto(
    val key: String,
    val title: String = "",
    @SerialName("author_name") val authorNames: List<String> = emptyList(),
    @SerialName("cover_i") val coverId: Int? = null,
    @SerialName("first_publish_year") val firstPublishYear: Int? = null,
    val publisher: List<String> = emptyList(),
    val isbn: List<String> = emptyList()
)
@Serializable
data class SearchResponseDto(
    val numFound: Int = 0,
    val docs: List<SearchDocDto> = emptyList()
)