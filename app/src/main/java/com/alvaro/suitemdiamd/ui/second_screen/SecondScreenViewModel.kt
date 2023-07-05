package com.alvaro.suitemdiamd.ui.second_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alvaro.suitemdiamd.data.pagging.SetPreference
import com.alvaro.suitemdiamd.data.pagging.Users

class SecondScreenViewModel(private val pref: SetPreference): ViewModel() {

    fun getUserName(): LiveData<Users> {
        return pref.getSelectedUsername().asLiveData()
    }
}