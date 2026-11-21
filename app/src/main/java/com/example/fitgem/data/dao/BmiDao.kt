package com.example.fitgem.data.dao

import androidx.room.*
import com.example.fitgem.data.entity.BmiRecordEntity

/**
 * BMI DAO — stores and retrieves BMI calculation records.
 */
@Dao
interface BmiDao {

    @Insert
    suspend fun insertBmi(bmi: BmiRecordEntity)

    /** Get the most recent BMI record. */
    @Query("SELECT * FROM bmi_records WHERE userId = :userId ORDER BY date DESC LIMIT 1")
    suspend fun getLatestBmi(userId: Int): BmiRecordEntity?

    @Query("SELECT * FROM bmi_records WHERE userId = :userId ORDER BY date DESC")
    suspend fun getAllBmiRecords(userId: Int): List<BmiRecordEntity>
}
