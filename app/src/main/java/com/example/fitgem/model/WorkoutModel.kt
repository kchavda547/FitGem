package com.example.fitgem.model

/**
 * WORKOUT MODEL — represents a workout plan card shown in the workout list.
 * This is NOT a database entity because workout plans are pre-defined in code.
 */
data class WorkoutModel(
    val id: Int,
    val name: String,
    val category: String,       // "Beginner" / "Intermediate" / "Advanced"
    val durationMins: Int,
    val exerciseCount: Int,
    val caloriesBurned: Int,
    val imageResId: Int,        // drawable resource ID
    val exercises: List<ExerciseModel>
)

/**
 * EXERCISE MODEL — represents one exercise within a workout plan.
 */
data class ExerciseModel(
    val name: String,
    val reps: String,           // e.g. "12 reps" or "30 sec"
    val durationSeconds: Int,   // countdown timer duration (for timed exercises use this; for reps use 0)
    val imageResId: Int         // drawable resource ID for the exercise image
)
