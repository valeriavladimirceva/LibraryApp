package com.example.libraryapp.domain.usecase

import com.example.libraryapp.domain.model.LessonTime
import com.example.libraryapp.domain.repository.ReminderScheduler
import com.example.libraryapp.domain.repository.ScheduleResult

class ScheduleReminderUseCase(private val scheduler: ReminderScheduler) {
    operator fun invoke(time: LessonTime, ownerName: String): ScheduleResult =
        scheduler.schedule(time, ownerName.trim())
}