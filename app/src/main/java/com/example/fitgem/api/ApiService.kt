package com.example.fitgem.api

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("api/login")
    fun login(@Body body: LoginRequest): Call<LoginResponse>

    @POST("api/register")
    fun register(@Body body: RegisterRequest): Call<RegisterResponse>

    @GET("api/dashboard")
    fun getDashboardData(@Query("user_id") userId: Int): Call<DashboardResponse>

    @GET("api/workouts")
    fun getWorkouts(@Query("category") category: String? = null): Call<List<WorkoutResponse>>

    @POST("api/workouts/history")
    fun logWorkout(@Body history: WorkoutHistoryRequest): Call<ApiResponse>

    @GET("api/diet")
    fun getDietLogs(@Query("user_id") userId: Int): Call<DietResponse>

    @POST("api/diet/meal")
    fun addMeal(@Body meal: MealRequest): Call<ApiResponse>

    @POST("api/water/add")
    fun addWater(@Body water: WaterRequest): Call<ApiResponse>

    @GET("api/goals")
    fun getGoals(@Query("user_id") userId: Int): Call<List<GoalResponse>>

    @POST("api/goals")
    fun saveGoal(@Body goal: GoalRequest): Call<ApiResponse>

    @POST("api/bmi")
    fun saveBmi(@Body bmi: BmiRequest): Call<ApiResponse>
}
