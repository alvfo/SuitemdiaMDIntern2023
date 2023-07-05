package com.alvaro.suitemdiamd.data.remote

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alvaro.suitemdiamd.data.api.UserResponseItem

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: List<UserResponseItem>)

    @Query("SELECT * FROM user")
    fun getUser(): PagingSource<Int, UserResponseItem>

    @Query("DELETE FROM user")
    suspend fun deleteUser()
}