package com.alvaro.suitemdiamd.data.api

import android.content.Context
import com.alvaro.suitemdiamd.data.pagging.UsersRepo
import com.alvaro.suitemdiamd.data.remote.UserDb

object Injection {
    fun provideRepo(context: Context): UsersRepo {
        val database = UserDb.getDatabase(context)
        val apiService = ApiConfigurates.getApiServices()
        return UsersRepo(database, apiService)
    }
}