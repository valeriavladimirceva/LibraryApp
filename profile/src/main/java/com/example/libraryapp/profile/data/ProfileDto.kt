package com.example.libraryapp.profile.data

import com.example.libraryapp.profile.domain.model.LessonTime
import com.example.libraryapp.profile.domain.model.Profile
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val fullName: String = "",
    val photoUri: String? = null,
    val resumeUrl: String = "",
    val lessonTime: String? = null
)

fun ProfileDto.toDomain() = Profile(
    fullName = fullName,
    photoUri = photoUri,
    resumeUrl = resumeUrl,
    lessonTime = lessonTime?.let { LessonTime.parse(it) }
)

fun Profile.toDto() = ProfileDto(
    fullName = fullName,
    photoUri = photoUri,
    resumeUrl = resumeUrl,
    lessonTime = lessonTime?.toString()
)