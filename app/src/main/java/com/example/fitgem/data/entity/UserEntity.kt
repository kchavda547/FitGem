package com.example.fitgem.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * USER entity — stores registered user accounts.
 * One record per registered user.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val mobile: String,
    val password: String,
    val age: Int = 0,
    val heightCm: Float = 0f,
    val weightKg: Float = 0f,
    val fitnessGoal: String = "Lose Weight"
)
