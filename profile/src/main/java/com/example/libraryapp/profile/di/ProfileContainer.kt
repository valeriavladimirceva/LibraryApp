package com.example.libraryapp.profile.di

import android.content.Context
import com.example.libraryapp.profile.data.DataStoreProfileRepository
import com.example.libraryapp.profile.data.DownloadManagerResumeRepository
import com.example.libraryapp.profile.data.FilePhotoStorage
import com.example.libraryapp.profile.data.notification.AlarmReminderScheduler
import com.example.libraryapp.profile.data.notification.NotificationHelper
import com.example.libraryapp.profile.domain.repository.PhotoStorage
import com.example.libraryapp.profile.domain.repository.ProfileRepository
import com.example.libraryapp.profile.domain.repository.ReminderScheduler
import com.example.libraryapp.profile.domain.repository.ResumeRepository
import com.example.libraryapp.profile.domain.usecase.CreateCameraTargetUseCase
import com.example.libraryapp.profile.domain.usecase.DownloadResumeUseCase
import com.example.libraryapp.profile.domain.usecase.ImportPhotoUseCase
import com.example.libraryapp.profile.domain.usecase.ObserveProfileUseCase
import com.example.libraryapp.profile.domain.usecase.SaveProfileUseCase
import com.example.libraryapp.profile.domain.usecase.ScheduleReminderUseCase
import com.example.libraryapp.profile.domain.usecase.ValidateLessonTimeUseCase

class ProfileContainer(context: Context) {

    private val appContext = context.applicationContext

    private val profileRepository: ProfileRepository = DataStoreProfileRepository(appContext)
    private val photoStorage: PhotoStorage = FilePhotoStorage(appContext)
    private val resumeRepository: ResumeRepository = DownloadManagerResumeRepository(appContext)
    private val reminderScheduler: ReminderScheduler = AlarmReminderScheduler(appContext)

    val observeProfile = ObserveProfileUseCase(profileRepository)
    val saveProfile = SaveProfileUseCase(profileRepository)
    val createCameraTarget = CreateCameraTargetUseCase(photoStorage)
    val importPhoto = ImportPhotoUseCase(photoStorage)
    val downloadResume = DownloadResumeUseCase(resumeRepository)
    val validateLessonTime = ValidateLessonTimeUseCase()
    val scheduleReminder = ScheduleReminderUseCase(reminderScheduler)

    init {
        NotificationHelper.createChannel(appContext)
    }
}