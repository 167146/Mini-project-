package com.example.lunacare.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lunacare.data.entities.User

@Dao
interface UserDao {

    @Insert
    suspend fun registerUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): User?
}
