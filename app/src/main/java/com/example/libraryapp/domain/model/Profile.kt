package com.example.libraryapp.domain.model

data class Profile(
    val fullName: String = "",
    val photoUri: String? = null,
    val resumeUrl: String = ""
)