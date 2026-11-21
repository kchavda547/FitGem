package com.example.fitgem.api

import com.google.gson.annotations.SerializedName

// ── Authentication Models ──
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("user_id") val userId: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("token") val token: String?
)

data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("user_id") val userId: Int?
)

// ── Generic API Response ──
data class ApiResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String
)

// ── Workout Models ──
data class WorkoutResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("category") val category: String,
    @SerializedName("duration_min") val durationMin: Int,
    @SerializedName("calories_burned") val caloriesBurned: Int,
    @SerializedName("level") val level: String,
    @SerializedName("exercises_count") val exercisesCount: Int,
    @SerializedName("image_url") val imageUrl: String?
)

data class WorkoutHistoryRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("workout_title") val workoutTitle: String,
    @SerializedName("duration_minutes") val durationMinutes: Int,
    @SerializedName("calories_burned") val caloriesBurned: Int,
    @SerializedName("completed_at") val completedAt: String
)

// ── Diet & Meal Models ──
data class MealRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("meal_type") val mealType: String,
    @SerializedName("food_name") val foodName: String,
    @SerializedName("calories") val calories: Int,
    @SerializedName("protein_g") val proteinG: Float,
    @SerializedName("carbs_g") val carbsG: Float,
    @SerializedName("fats_g") val fatsG: Float
)

data class DietResponse(
    @SerializedName("consumed_calories") val consumedCalories: Int,
    @SerializedName("target_calories") val targetCalories: Int,
    @SerializedName("protein_g") val proteinG: Float,
    @SerializedName("carbs_g") val carbsG: Float,
    @SerializedName("fats_g") val fatsG: Float,
    @SerializedName("meals") val meals: List<MealItem>
)

data class MealItem(
    @SerializedName("id") val id: Int,
    @SerializedName("meal_type") val mealType: String,
    @SerializedName("food_name") val foodName: String,
    @SerializedName("calories") val calories: Int
)

// ── Water Models ──
data class WaterRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("amount_ml") val amountMl: Int
)

// ── Goal Models ──
data class GoalRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("goal_type") val goalType: String,
    @SerializedName("target_value") val targetValue: Float,
    @SerializedName("current_value") val currentValue: Float
)

data class GoalResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("goal_type") val goalType: String,
    @SerializedName("target_value") val targetValue: Float,
    @SerializedName("current_value") val currentValue: Float,
    @SerializedName("progress_percent") val progressPercent: Float
)

// ── BMI Models ──
data class BmiRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("height_cm") val heightCm: Float,
    @SerializedName("weight_kg") val weightKg: Float,
    @SerializedName("bmi_score") val bmiScore: Float,
    @SerializedName("category") val category: String
)

// ── Dashboard Data Response ──
data class DashboardResponse(
    @SerializedName("today_goal_percent") val todayGoalPercent: Float,
    @SerializedName("workout_min") val workoutMin: Int,
    @SerializedName("target_workout_min") val targetWorkoutMin: Int,
    @SerializedName("calories_burned") val caloriesBurned: Int,
    @SerializedName("steps") val steps: Int,
    @SerializedName("target_steps") val targetSteps: Int,
    @SerializedName("water_ml") val waterMl: Int,
    @SerializedName("target_water_ml") val targetWaterMl: Int
)
