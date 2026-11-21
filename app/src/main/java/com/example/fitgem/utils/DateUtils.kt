package com.example.fitgem.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * DATE UTILS — helper functions for date formatting used throughout the app.
 */
object DateUtils {

    private val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val sdfDisplay = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    /** Returns today's date as "yyyy-MM-dd" (for DB storage). */
    fun today(): String = sdfDate.format(Date())

    /** Returns the date N days ago as "yyyy-MM-dd". */
    fun daysAgo(days: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -days)
        return sdfDate.format(cal.time)
    }

    /** Formats a stored date string to a user-friendly display format. */
    fun formatForDisplay(dateStr: String): String {
        return try {
            val date = sdfDate.parse(dateStr)
            if (date != null) sdfDisplay.format(date) else dateStr
        } catch (e: Exception) {
            dateStr
        }
    }

    /** Returns the day-of-week short label for a "yyyy-MM-dd" string. */
    fun getDayLabel(dateStr: String): String {
        return try {
            val date = sdfDate.parse(dateStr) ?: return ""
            SimpleDateFormat("EEE", Locale.getDefault()).format(date)
        } catch (e: Exception) {
            ""
        }
    }
}
