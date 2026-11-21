package com.example.fitgem.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * GOAL entity — stores fitness goals set by the user.
 */
@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val goalName: String,
    val targetValue: String,       // e.g. "60 kg" or "5 workouts/week"
    val currentProgress: Int = 0,  // 0–100 percentage
    val startDate: String,         // "yyyy-MM-dd"
    val targetDate: String         // "yyyy-MM-dd"
)
