package com.example.prosanders020.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_users")
data class User(
    @PrimaryKey(autoGenerate = true) val nim: String,
    val nama: String,
    val password: String,
)
