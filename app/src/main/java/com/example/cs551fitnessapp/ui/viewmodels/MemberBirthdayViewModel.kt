package com.example.cs551fitnessapp.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MemberBirthdayViewModel : ViewModel() {

    var birthday = mutableStateOf("")
        private set

    fun updateBirthday(newDate: String) {

        birthday.value = newDate

    }

}