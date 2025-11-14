package com.example.prosanders020.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_users")
data class User(
    @PrimaryKey(autoGenerate = true) val nim: Int,
    val nama: String,
    val passwordHash: String,
)
