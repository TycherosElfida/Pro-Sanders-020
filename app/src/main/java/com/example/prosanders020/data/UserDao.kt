package com.example.prosanders020.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM tbl_users WHERE nim = :nim AND password = :password")
    suspend fun login(nim: String, password: String): User?


}