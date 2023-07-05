package com.alvaro.suitemdiamd.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvaro.suitemdiamd.data.api.Injection
import com.alvaro.suitemdiamd.data.pagging.SetPreference
import com.alvaro.suitemdiamd.ui.first_screen.FirstScreenViewModel
import com.alvaro.suitemdiamd.ui.second_screen.SecondScreenViewModel
import com.alvaro.suitemdiamd.ui.third_screen.ThirdScreenViewModel

class VMFactory(private val pref: SetPreference, private val context: Context) :
    ViewModelProvider.Factory {

        @Suppress("UNCHECKED")
        override fun <T : ViewModel> create (viewModelClass: Class<T>): T =
            when{
                viewModelClass.isAssignableFrom(FirstScreenViewModel::class.java) -> {
                    FirstScreenViewModel(pref) as T
                }
                viewModelClass.isAssignableFrom(SecondScreenViewModel::class.java) -> {
                    SecondScreenViewModel(pref) as T
                }
                viewModelClass.isAssignableFrom(ThirdScreenViewModel::class.java) -> {
                    ThirdScreenViewModel(pref, Injection.provideRepo(context)) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: " + viewModelClass.name)
            }
}