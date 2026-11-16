package com.example.libraryapp.domain.repository

import com.example.libraryapp.domain.model.LessonTime

enum class ScheduleResult { Scheduled, ExactAlarmNotAllowed }

interface ReminderScheduler {
    fun schedule(time: LessonTime, ownerName: String): ScheduleResult
}