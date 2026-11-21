package com.example.fitgem.data.dao

import androidx.room.*
import com.example.fitgem.data.entity.GoalEntity

/**
 * GOAL DAO — stores and retrieves fitness goals.
 */
@Dao
interface GoalDao {

    @Insert
    suspend fun insertGoal(goal: GoalEntity)

    @Update
    suspend fun updateGoal(goal: GoalEntity)

    @Delete
    suspend fun deleteGoal(goal: GoalEntity)

    @Query("SELECT * FROM goals WHERE userId = :userId ORDER BY id DESC")
    suspend fun getAllGoals(userId: Int): List<GoalEntity>

    @Query("SELECT * FROM goals WHERE id = :id LIMIT 1")
    suspend fun getGoalById(id: Int): GoalEntity?

    @Query("SELECT COUNT(*) FROM goals WHERE userId = :userId")
    suspend fun getGoalCount(userId: Int): Int
}
