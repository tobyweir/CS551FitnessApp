package com.example.cs551fitnessapp.ui

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cs551fitnessapp.FitnessApplication
import com.example.cs551fitnessapp.database.DatabaseModule
import com.example.cs551fitnessapp.ui.viewmodels.EditMemberViewModel
import com.example.cs551fitnessapp.ui.viewmodels.MemberViewModel
import com.example.cs551fitnessapp.ui.viewmodels.MembersViewModel
import com.example.cs551fitnessapp.ui.viewmodels.TodayViewModel
import com.example.cs551fitnessapp.ui.viewmodels.AddMemberViewModel
import com.example.cs551fitnessapp.ui.viewmodels.WorkoutPlanViewModel
import com.example.cs551fitnessapp.ui.viewmodels.SearchWorkoutViewModel

object ViewModelFactory {

    @RequiresApi(Build.VERSION_CODES.O)
    val Factory = viewModelFactory {
        initializer {
            MembersViewModel(this[APPLICATION_KEY] as Application)
        }
        initializer {
            val application = this.fitnessApplication()
            MemberViewModel(application)
        }
        initializer {
            val application = this.fitnessApplication()
            val repository = DatabaseModule.provideRepository(application)
            TodayViewModel(repository = repository)
        }
        initializer {
            EditMemberViewModel()
        }
        initializer {
            AddMemberViewModel(this[APPLICATION_KEY] as Application)
        }

        initializer {
            val application = this.fitnessApplication()
            WorkoutPlanViewModel(application)
        }

        initializer {
            val application = this.fitnessApplication()
            SearchWorkoutViewModel(application)
        }
    }
}

fun CreationExtras.fitnessApplication(): FitnessApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as FitnessApplication)