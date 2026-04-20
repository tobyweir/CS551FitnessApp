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

    fun toDisplayString(epochMillis: Long): String {
        val cal = Calendar.getInstance().apply { timeInMillis = epochMillis }
        return "%02d %s %d  %02d:%02d".format(
            cal.get(Calendar.DAY_OF_MONTH),
            monthNames[cal.get(Calendar.MONTH)],
            cal.get(Calendar.YEAR),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE)
        )
    }

    fun getDayRangebyCalendar(dateStr: String): Pair<Long, Long> {
        val parts    = dateStr.split(" ")
        val day      = parts[0].toInt()
        val month    = listOf("Jan","Feb","Mar","Apr","May","Jun",
            "Jul","Aug","Sep","Oct","Nov","Dec")
            .indexOf(parts[1])
        val year     = parts[2].toInt()

        val cal = Calendar.getInstance().apply {
            set(year, month, day, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val startOfDay = cal.timeInMillis    // 00:00:00.000

        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)

        val endOfDay = cal.timeInMillis     // 23:59:59.999

        return Pair(startOfDay, endOfDay)
    }
}