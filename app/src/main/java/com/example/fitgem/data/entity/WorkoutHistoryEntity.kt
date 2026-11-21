package com.example.fitgem.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * WORKOUT HISTORY entity — records each completed workout session.
 */
@Entity(tableName = "workout_history")
data class WorkoutHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val workoutName: String,
    val exercisesCompleted: Int,
    val durationMins: Int,
    val caloriesBurned: Int,
    val date: String  // stored as "yyyy-MM-dd"
)
