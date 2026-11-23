package com.example.libraryapp.profile.domain.model

sealed interface LessonTimeValidation {
    data class Valid(val time: LessonTime) : LessonTimeValidation
    data object Empty : LessonTimeValidation
    data object InvalidFormat : LessonTimeValidation
}