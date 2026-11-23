package com.example.libraryapp.profile.domain.usecase

import com.example.libraryapp.profile.domain.model.LessonTime
import com.example.libraryapp.profile.domain.repository.ReminderScheduler
import com.example.libraryapp.profile.domain.repository.ScheduleResult

class ScheduleReminderUseCase(private val scheduler: ReminderScheduler) {
    operator fun invoke(time: LessonTime, ownerName: String): ScheduleResult =
        scheduler.schedule(time, ownerName.trim())
}