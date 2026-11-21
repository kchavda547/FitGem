package com.example.fitgem.utils

/**
 * VALIDATION UTILS
 *
 * Reusable validation functions used across all forms in the app.
 * Each function returns null if valid, or an error message string if invalid.
 */
object ValidationUtils {

    /** Returns null if name is valid, otherwise an error message. */
    fun validateName(name: String): String? {
        if (name.isBlank()) return "Please enter your name"
        if (name.length < 2) return "Name must be at least 2 characters"
        return null
    }

    /** Returns null if email is valid. */
    fun validateEmail(email: String): String? {
        if (email.isBlank()) return "Please enter your email"
        val emailPattern = Regex("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")
        if (!emailPattern.matches(email)) return "Please enter a valid email address"
        return null
    }

    /** Returns null if mobile number is valid (10 digits). */
    fun validateMobile(mobile: String): String? {
        if (mobile.isBlank()) return "Please enter your mobile number"
        if (!mobile.matches(Regex("^[0-9]{10}$"))) return "Please enter a valid 10-digit mobile number"
        return null
    }

    /** Returns null if password is valid (min 6 chars). */
    fun validatePassword(password: String): String? {
        if (password.isBlank()) return "Please enter a password"
        if (password.length < 6) return "Password must be at least 6 characters"
        return null
    }

    /** Returns null if passwords match. */
    fun validatePasswordMatch(password: String, confirm: String): String? {
        if (confirm.isBlank()) return "Please confirm your password"
        if (password != confirm) return "Passwords do not match"
        return null
    }

    /** Returns null if the value is a positive number. */
    fun validatePositiveNumber(value: String, fieldName: String): String? {
        if (value.isBlank()) return "Please enter $fieldName"
        val num = value.toDoubleOrNull() ?: return "Please enter a valid number for $fieldName"
        if (num <= 0) return "$fieldName must be greater than 0"
        return null
    }

    /** Returns null if the value is not blank, otherwise an error message. */
    fun validateRequired(value: String, fieldName: String): String? {
        if (value.isBlank()) return "Please enter $fieldName"
        return null
    }
}
