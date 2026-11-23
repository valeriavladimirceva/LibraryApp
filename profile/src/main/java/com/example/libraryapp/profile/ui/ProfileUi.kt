package com.example.libraryapp.profile.ui

import com.example.libraryapp.profile.domain.model.Profile

data class ProfileUi(
    val fullName: String = "",
    val photoUri: String? = null,
    val hasResume: Boolean = false
)

fun Profile.toUi() = ProfileUi(
    fullName = fullName.trim(),
    photoUri = photoUri,
    hasResume = resumeUrl.isNotBlank()
)