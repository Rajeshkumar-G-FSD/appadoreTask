package com.example.flagquiz

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.Toast
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Utils {
    private var toast: Toast? = null

    fun showToast(context: Context?, message: String?) {
        if (toast != null) {
            toast!!.cancel()
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    @SuppressLint("DefaultLocale")
    fun getTimeDuration(targetTime: String?): String {
        // Current time
        var now: LocalDateTime? = null
        var targetLocalTime: LocalTime? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now = LocalDateTime.now()
        }
        var targetDateTime: LocalDateTime? = null

        // Parse the target time (example: "15:30:00")
        var timeFormatter: DateTimeFormatter? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            targetLocalTime = LocalTime.parse(targetTime, timeFormatter)
        }

        // Determine if the target time is for today or tomorrow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            targetDateTime = if (now!!.toLocalTime().isBefore(targetLocalTime)) {
                now.with(targetLocalTime)
            } else {
                now.plusDays(1).with(targetLocalTime)
            }
        }

        // Calculate the duration between now and the target time
        var duration: Duration? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            duration = Duration.between(now, targetDateTime)
        }

        var hours: Long = 0
        var minutes: Long = 0
        var seconds: Long = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            hours = duration!!.toHours()
            minutes = duration.toMinutes() % 60
            seconds = duration.seconds % 60
        }


        // Format the target date-time
        var dateTimeFormatter: DateTimeFormatter? = null
        var formattedTargetDateTime: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            formattedTargetDateTime = targetDateTime!!.format(dateTimeFormatter)
        }

        // Return formatted duration and target date-time
        return String.format(
            "Duration: %02d:%02d:%02d, Target DateTime: %s",
            hours,
            minutes,
            seconds,
            formattedTargetDateTime
        )
    }
}
