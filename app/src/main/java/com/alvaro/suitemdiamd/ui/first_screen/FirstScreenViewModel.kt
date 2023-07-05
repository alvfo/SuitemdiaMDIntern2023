package com.alvaro.suitemdiamd.ui.first_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvaro.suitemdiamd.data.pagging.SetPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FirstScreenViewModel (private val pref:SetPreference): ViewModel(){
    private val _isPalindrome = MutableStateFlow(false)
    val isPalindrome = _isPalindrome.asStateFlow()

    fun isPalindrome(input: String){
        val reverse = input.reversed()
        _isPalindrome.value = input.equals(reverse,ignoreCase = true)
    }
    fun saveInsertName(insertName: String){
        viewModelScope.launch { pref.saveName(insertName) }
    }

}