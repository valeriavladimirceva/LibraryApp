package com.example.libraryapp.profile.domain.repository

import com.example.libraryapp.profile.domain.model.LessonTime

enum class ScheduleResult { Scheduled, ExactAlarmNotAllowed }

interface ReminderScheduler {
    fun schedule(time: LessonTime, ownerName: String): ScheduleResult
}