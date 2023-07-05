package com.alvaro.suitemdiamd.ui.third_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alvaro.suitemdiamd.data.api.UserResponseItem
import com.alvaro.suitemdiamd.data.pagging.SetPreference
import com.alvaro.suitemdiamd.data.pagging.UsersRepo
import kotlinx.coroutines.launch

class ThirdScreenViewModel(private val pref: SetPreference, usersRepo: UsersRepo): ViewModel() {

    val users: LiveData<PagingData<UserResponseItem>> = usersRepo.getUser().cachedIn(viewModelScope)

    fun showSelectedUser(name: String){
        viewModelScope.launch {
            pref.saveSelected(name)
        }
    }
}