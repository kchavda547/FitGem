package com.example.fitgem.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * BODY MEASUREMENT entity — stores body measurement records over time.
 */
@Entity(tableName = "body_measurements")
data class BodyMeasurementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val weightKg: Float,
    val chestCm: Float,
    val waistCm: Float,
    val hipsCm: Float,
    val armsCm: Float,
    val thighsCm: Float,
    val date: String  // "yyyy-MM-dd"
)
