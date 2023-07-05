package com.alvaro.suitemdiamd.data.pagging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.alvaro.suitemdiamd.data.api.ApiServices
import com.alvaro.suitemdiamd.data.api.UserResponseItem
import com.alvaro.suitemdiamd.data.remote.RemoteKey
import com.alvaro.suitemdiamd.data.remote.UserDb

@OptIn(ExperimentalPagingApi::class)
class UsersRemoteMediator(val database: UserDb, val apiServices: ApiServices): RemoteMediator<Int,UserResponseItem>() {
    private companion object{
        const val INITIAL_PAGE = 1
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserResponseItem>
    ): MediatorResult {
        val page = when (loadType){
            LoadType.APPEND ->{
                val remoteKey = getLastItem(state)
                remoteKey?.
                nextKey?:
                return MediatorResult.Success(endOfPaginationReached = remoteKey!= null)
            }
            LoadType.PREPEND -> {
                val remoteKey = getFirstItem(state)
                remoteKey?.
                prevKey?:
                return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
            }
            LoadType.REFRESH ->{
                val remoteKey = getClosestToCurrentPosition(state)
                remoteKey?.
                nextKey?.minus(1)?: INITIAL_PAGE
            }
        }

        try {
            val response = apiServices.getAllUsers(page,state.config.pageSize).data
            val end = response.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH){
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.userDao().deleteUser()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (end) null else page + 1
                val key = response.map {
                    RemoteKey(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(key)
                database.userDao().insertUser(response)
            }
            return MediatorResult.Success(endOfPaginationReached = end)
        } catch (exception: Exception){
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getLastItem(state: PagingState<Int, UserResponseItem>) =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }

    private suspend fun getFirstItem(state: PagingState<Int, UserResponseItem>) =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }

    private suspend fun getClosestToCurrentPosition(state: PagingState<Int, UserResponseItem>) =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
}