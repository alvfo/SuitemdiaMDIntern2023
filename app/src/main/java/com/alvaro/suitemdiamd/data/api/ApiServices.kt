package com.alvaro.suitemdiamd.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("users")
    suspend fun getAllUsers(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
    ): UsersResponse
}