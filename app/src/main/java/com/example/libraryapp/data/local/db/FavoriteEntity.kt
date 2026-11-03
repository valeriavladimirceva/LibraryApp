package com.example.libraryapp.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val title: String,
    val authors: List<String>,
    val coverId: Int?,
    val firstPublishYear: Int?,
    val publishers: List<String>,
    val isbn: String?,
    val description: String,
    val subjects: List<String>,
    val addedAt: Long
)