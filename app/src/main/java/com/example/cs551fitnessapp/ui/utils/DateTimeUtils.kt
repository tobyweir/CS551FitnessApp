package com.example.cs551fitnessapp.ui.utils

import java.util.Calendar

object DateTimeUtils {

    private val monthNames = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )
    fun DtToEpochMillis(
        dateStr : String,
        hour    : Int,
        minute  : Int
    ): Long {

        val monthNames = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )

        // Parse "12 Apr 2026" to day=12, month=3 , year=2026
        val parts = dateStr.trim().split(" ")
        require(parts.size == 3) { "Invalid date format: $dateStr. Expected 'DD MMM YYYY'" }

        val day = parts[0].toInt()
        val month = monthNames.indexOfFirst {
            it.equals(parts[1], ignoreCase = true)
        }.also { require(it >= 0) { "Invalid month: ${parts[1]}" } }
        val year = parts[2].toInt()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
    }

    fun TimeDurationMinute(
        startHour : Int,
        startMin  : Int,
        endHour   : Int,
        endMin    : Int
    ): Int {
        val startTotalMin = startHour * 60 + startMin
        val endTotalMin   = endHour   * 60 + endMin

        return if (endTotalMin >= startTotalMin) {
            // Normal:    07:30 → 09:00 = 90 min
            endTotalMin - startTotalMin
        } else {
            // Overnight: 23:30 → 00:30 = 60 min
            (24 * 60 - startTotalMin) + endTotalMin
        }
    }
}