package com.example.libraryapp.di

import android.app.Application
import com.example.libraryapp.data.notification.NotificationHelper

class LibraryApplication : Application() {
    val container by lazy { AppContainer(this) }

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createChannel(this)
    }
}