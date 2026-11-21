package com.example.fitgem.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * BMI RECORD entity — stores each BMI calculation result.
 */
@Entity(tableName = "bmi_records")
data class BmiRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val heightCm: Float,
    val weightKg: Float,
    val bmiValue: Float,
    val category: String,   // "Underweight" / "Normal Weight" / "Overweight" / "Obese"
    val date: String        // "yyyy-MM-dd"
)
