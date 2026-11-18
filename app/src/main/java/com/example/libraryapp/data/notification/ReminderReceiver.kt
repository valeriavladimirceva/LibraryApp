package com.example.libraryapp.data.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val ownerName = intent.getStringExtra(EXTRA_OWNER_NAME).orEmpty()
        NotificationHelper.showLessonReminder(context, ownerName)
    }

    companion object {
        const val EXTRA_OWNER_NAME = "owner_name"
    }
}