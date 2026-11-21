package com.example.fitgem.data.dao

import androidx.room.*
import com.example.fitgem.data.entity.UserEntity

/**
 * USER DAO — all database operations for the users table.
 */
@Dao
interface UserDao {

    /** Insert a new user. Returns the auto-generated row ID. */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long

    /** Update an existing user record. */
    @Update
    suspend fun updateUser(user: UserEntity)

    /** Find user by email — used during login. */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    /** Find user by primary key — used to load profile. */
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): UserEntity?

    /** Check if an email is already registered. */
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun countByEmail(email: String): Int
}
