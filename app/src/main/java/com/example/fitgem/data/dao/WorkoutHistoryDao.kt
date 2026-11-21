package com.example.fitgem.data.dao

import androidx.room.*
import com.example.fitgem.data.entity.WorkoutHistoryEntity

/**
 * WORKOUT HISTORY DAO — stores and retrieves completed workout sessions.
 */
@Dao
interface WorkoutHistoryDao {

    @Insert
    suspend fun insertWorkout(workout: WorkoutHistoryEntity)

    /** Get all workouts for a user, newest first. */
    @Query("SELECT * FROM workout_history WHERE userId = :userId ORDER BY date DESC")
    suspend fun getAllWorkouts(userId: Int): List<WorkoutHistoryEntity>

    /** Get workouts logged in the last 7 days (for the progress chart). */
    @Query("""
        SELECT * FROM workout_history
        WHERE userId = :userId AND date >= :sevenDaysAgo
        ORDER BY date ASC
    """)
    suspend fun getWorkoutsLastSevenDays(userId: Int, sevenDaysAgo: String): List<WorkoutHistoryEntity>

    /** Get workouts logged on a specific date (for dashboard). */
    @Query("SELECT * FROM workout_history WHERE userId = :userId AND date = :date")
    suspend fun getWorkoutsOnDate(userId: Int, date: String): List<WorkoutHistoryEntity>

    /** Total calories burned by a user. */
    @Query("SELECT SUM(caloriesBurned) FROM workout_history WHERE userId = :userId")
    suspend fun getTotalCalories(userId: Int): Int?

    /** Total number of workouts completed. */
    @Query("SELECT COUNT(*) FROM workout_history WHERE userId = :userId")
    suspend fun getTotalWorkouts(userId: Int): Int
}
