package com.example.libraryapp.profile.domain.model

data class Profile(
    val fullName: String = "",
    val photoUri: String? = null,
    val resumeUrl: String = "",
    val lessonTime: LessonTime? = null
)