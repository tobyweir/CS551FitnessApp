package com.example.cs551fitnessapp.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cs551fitnessapp.FitnessApplication
import com.example.cs551fitnessapp.database.DatabaseModule
import com.example.cs551fitnessapp.ui.viewmodels.EditMemberViewModel
import com.example.cs551fitnessapp.ui.viewmodels.MemberViewModel
import com.example.cs551fitnessapp.ui.viewmodels.MembersViewModel
import com.example.cs551fitnessapp.ui.viewmodels.TodayViewModel

object ViewModelFactory {

    @RequiresApi(Build.VERSION_CODES.O)
    val Factory = viewModelFactory {
        initializer {
            MembersViewModel()
        }
        initializer {
            MemberViewModel()
        }
        initializer {
            val application = this.fitnessApplication()
            val repository = DatabaseModule.provideRepository(application)
            TodayViewModel(repository = repository)
        }
        initializer {
            EditMemberViewModel()
        }
    }
}

fun CreationExtras.fitnessApplication(): FitnessApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as FitnessApplication)
