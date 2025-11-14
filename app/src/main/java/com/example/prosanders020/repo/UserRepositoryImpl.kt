package com.example.prosanders020.repo

import com.example.prosanders020.data.User
import com.example.prosanders020.data.UserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao
) : UserRepository {

    override fun getAllUsers(): Flow<List<User>> = dao.getAllUsers()

    override suspend fun getUserWithNim(nim: String): User? = dao.getUserWithNim(nim)

    override suspend fun register(user: User) {
        dao.insertUser(user)
    }

    override suspend fun deleteUser(user: User) {
        dao.deleteUser(user)
    }
}