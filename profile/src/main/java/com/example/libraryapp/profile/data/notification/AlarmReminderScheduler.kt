package com.example.libraryapp.profile.data.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.libraryapp.profile.domain.model.LessonTime
import com.example.libraryapp.profile.domain.repository.ReminderScheduler
import com.example.libraryapp.profile.domain.repository.ScheduleResult
import java.util.Calendar

class AlarmReminderScheduler(private val context: Context) : ReminderScheduler {

    override fun schedule(time: LessonTime, ownerName: String): ScheduleResult {
        val alarmManager = context.getSystemService(AlarmManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !alarmManager.canScheduleExactAlarms()
        ) {
            return ScheduleResult.ExactAlarmNotAllowed
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            Intent(context, ReminderReceiver::class.java)
                .putExtra(ReminderReceiver.EXTRA_OWNER_NAME, ownerName),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                nextTriggerMillis(time),
                pendingIntent
            )
            ScheduleResult.Scheduled
        } catch (e: SecurityException) {
            ScheduleResult.ExactAlarmNotAllowed
        }
    }

    private fun nextTriggerMillis(time: LessonTime): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, time.hour)
            set(Calendar.MINUTE, time.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar.timeInMillis
    }

    private companion object {
        const val REQUEST_CODE = 2001
    }
}