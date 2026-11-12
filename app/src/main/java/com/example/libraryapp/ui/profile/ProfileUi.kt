package com.example.libraryapp.ui.profile

import com.example.libraryapp.domain.model.Profile

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