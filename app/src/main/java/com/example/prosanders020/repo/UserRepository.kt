package com.example.prosanders020.repo

import com.example.prosanders020.data.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers(): Flow<List<User>>
    suspend fun getUserWithNim(nim: String): User?
    suspend fun register(user: User)
    suspend fun deleteUser(user: User)
    suspend fun updateUser(user: User)

}