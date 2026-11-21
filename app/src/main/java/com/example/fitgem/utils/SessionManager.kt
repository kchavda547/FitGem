package com.example.fitgem.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME       = "fitgem_session"
        private const val KEY_IS_LOGGED   = "is_logged_in"
        private const val KEY_USER_ID     = "user_id"
        private const val KEY_USER_NAME   = "user_name"
        private const val KEY_USER_EMAIL  = "user_email"
        private const val KEY_REMEMBER_ME = "remember_me"
        
        // Settings
        private const val KEY_THEME_MODE   = "theme_mode"     // "light", "dark", "system"
        private const val KEY_UNIT_WEIGHT  = "unit_weight"    // "kg" or "lb"
        private const val KEY_UNIT_HEIGHT  = "unit_height"    // "cm" or "ft"
        private const val KEY_REMINDERS    = "reminders_on"
        private const val KEY_NOTIFS       = "notifications_on"

        const val THEME_LIGHT  = "light"
        const val THEME_DARK   = "dark"
        const val THEME_SYSTEM = "system"
    }

    // ─── Session ────────────────────────────────────────────────────────────

    fun saveLogin(userId: Int, name: String, email: String, rememberMe: Boolean = false) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED, true)
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            putBoolean(KEY_REMEMBER_ME, rememberMe)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED, false)
    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, 1)
    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "Kesu") ?: "Kesu"
    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "kesu@fitness.com") ?: "kesu@fitness.com"
    fun isRememberMe(): Boolean = prefs.getBoolean(KEY_REMEMBER_ME, false)

    fun updateUserName(name: String) {
        prefs.edit().putString(KEY_USER_NAME, name).apply()
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    // ─── Theme Management ───────────────────────────────────────────────────

    fun getThemeMode(): String = prefs.getString(KEY_THEME_MODE, THEME_LIGHT) ?: THEME_LIGHT

    fun setThemeMode(mode: String) {
        prefs.edit().putString(KEY_THEME_MODE, mode).apply()
        applyThemeMode(mode)
    }

    fun applyThemeMode(mode: String = getThemeMode()) {
        when (mode) {
            THEME_LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            THEME_DARK  -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            THEME_SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    // ─── Settings ───────────────────────────────────────────────────────────

    fun getWeightUnit(): String  = prefs.getString(KEY_UNIT_WEIGHT, "kg") ?: "kg"
    fun getHeightUnit(): String  = prefs.getString(KEY_UNIT_HEIGHT, "cm") ?: "cm"
    fun isRemindersOn(): Boolean = prefs.getBoolean(KEY_REMINDERS, true)
    fun isNotificationsOn(): Boolean = prefs.getBoolean(KEY_NOTIFS, true)

    fun setWeightUnit(unit: String)    { prefs.edit().putString(KEY_UNIT_WEIGHT, unit).apply() }
    fun setHeightUnit(unit: String)    { prefs.edit().putString(KEY_UNIT_HEIGHT, unit).apply() }
    fun setReminders(on: Boolean)      { prefs.edit().putBoolean(KEY_REMINDERS, on).apply() }
    fun setNotifications(on: Boolean)  { prefs.edit().putBoolean(KEY_NOTIFS, on).apply() }
}
