package com.example.cs551fitnessapp.ui.viewmodels

import android.app.Application

import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.AndroidViewModel

import androidx.lifecycle.viewModelScope

import com.example.cs551fitnessapp.database.provideRepository

import kotlinx.coroutines.flow.collectLatest

import kotlinx.coroutines.launch



class ThemeViewModel(

    application: Application

) : AndroidViewModel(application) {



    private val repository =

        application.provideRepository()



    var isDarkTheme =

        mutableStateOf(false)

        private set



    init {

        viewModelScope.launch {

            repository.isDarkThemeEnabled.collectLatest {

                isDarkTheme.value = it

            }

        }

    }



    fun toggleTheme(enabled: Boolean) {

        isDarkTheme.value = enabled



        viewModelScope.launch {

            repository.setDarkThemeEnabled(enabled)

        }

    }

}