package com.alvaro.suitemdiamd.data.pagging

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.alvaro.suitemdiamd.data.api.ApiServices
import com.alvaro.suitemdiamd.data.api.UserResponseItem
import com.alvaro.suitemdiamd.data.remote.UserDb

class UsersRepo(private val userDb: UserDb, private val apiServices: ApiServices) {
    fun getUser(): LiveData<PagingData<UserResponseItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = UsersRemoteMediator(userDb, apiServices),
            pagingSourceFactory = {
                userDb.userDao().getUser()
            }
        ).liveData
    }
}
