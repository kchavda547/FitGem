package com.example.fitgem.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fitgem.data.dao.*
import com.example.fitgem.data.entity.*

/**
 * FITGEN DATABASE
 *
 * The main Room database for the FitGen app.
 * Uses a singleton pattern to avoid creating multiple instances.
 *
 * To access the database from an Activity:
 *   val db = FitGenDatabase.getDatabase(context)
 *   val userDao = db.userDao()
 */
@Database(
    entities = [
        UserEntity::class,
        WorkoutHistoryEntity::class,
        BodyMeasurementEntity::class,
        GoalEntity::class,
        BmiRecordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FitGenDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun workoutHistoryDao(): WorkoutHistoryDao
    abstract fun bodyMeasurementDao(): BodyMeasurementDao
    abstract fun goalDao(): GoalDao
    abstract fun bmiDao(): BmiDao

    companion object {
        // Volatile ensures all threads see the same instance
        @Volatile
        private var INSTANCE: FitGenDatabase? = null

        /**
         * Returns the database instance, creating it if needed.
         * Thread-safe via double-checked locking.
         */
        fun getDatabase(context: Context): FitGenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitGenDatabase::class.java,
                    "fitgen_database"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true) // for development; remove in production
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
