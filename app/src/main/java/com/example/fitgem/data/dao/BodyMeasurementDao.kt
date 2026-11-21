package com.example.fitgem.data.dao

import androidx.room.*
import com.example.fitgem.data.entity.BodyMeasurementEntity

/**
 * BODY MEASUREMENT DAO — stores and retrieves body measurement records.
 */
@Dao
interface BodyMeasurementDao {

    @Insert
    suspend fun insertMeasurement(measurement: BodyMeasurementEntity)

    /** Get the most recent measurement record. */
    @Query("SELECT * FROM body_measurements WHERE userId = :userId ORDER BY date DESC LIMIT 1")
    suspend fun getLatestMeasurement(userId: Int): BodyMeasurementEntity?

    /** Get all measurement records for chart. */
    @Query("SELECT * FROM body_measurements WHERE userId = :userId ORDER BY date ASC")
    suspend fun getAllMeasurements(userId: Int): List<BodyMeasurementEntity>

    /** Get the first (initial) measurement ever recorded. */
    @Query("SELECT * FROM body_measurements WHERE userId = :userId ORDER BY date ASC LIMIT 1")
    suspend fun getFirstMeasurement(userId: Int): BodyMeasurementEntity?
}
