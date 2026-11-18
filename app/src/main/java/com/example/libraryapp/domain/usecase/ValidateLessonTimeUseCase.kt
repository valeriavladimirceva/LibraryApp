package com.example.libraryapp.domain.usecase

import com.example.libraryapp.domain.model.LessonTime
import com.example.libraryapp.domain.model.LessonTimeValidation

class ValidateLessonTimeUseCase {
    operator fun invoke(text: String): LessonTimeValidation {
        if (text.isBlank()) return LessonTimeValidation.Empty
        val time = LessonTime.parse(text) ?: return LessonTimeValidation.InvalidFormat
        return LessonTimeValidation.Valid(time)
    }
}