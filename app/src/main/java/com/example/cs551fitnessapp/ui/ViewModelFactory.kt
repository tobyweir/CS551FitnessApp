package com.example.cs551fitnessapp.ui
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cs551fitnessapp.ui.viewmodels.EditMemberViewModel
import com.example.cs551fitnessapp.ui.viewmodels.MemberViewModel
import com.example.cs551fitnessapp.ui.viewmodels.MembersViewModel
import com.example.cs551fitnessapp.ui.viewmodels.TodayViewModel

object ViewModelFactory {

    val Factory = viewModelFactory {
        initializer {
            MembersViewModel()
        }
        initializer {
            MemberViewModel()
        }
        initializer {
            TodayViewModel()
        }
        initializer {
            EditMemberViewModel()
        }
    }
}