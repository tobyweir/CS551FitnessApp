package com.example.cs551fitnessapp.ui.navigation

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cs551fitnessapp.database.WorkoutPlanData
import com.example.cs551fitnessapp.ui.screens.SearchWorkoutScreen
import com.example.cs551fitnessapp.ui.screens.WorkoutInfoScreen
import com.example.cs551fitnessapp.ui.screens.WorkoutPlanScreen
import com.example.cs551fitnessapp.ui.viewmodels.WorkoutPlanViewModel
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.example.cs551fitnessapp.ui.screens.BirthdayScreen
import com.example.cs551fitnessapp.ui.screens.HeightScreen
import com.example.cs551fitnessapp.ui.screens.MedicalConcernScreen
import com.example.cs551fitnessapp.ui.screens.MemberGoalScreen
import com.example.cs551fitnessapp.ui.screens.NewMemberSexScreen
import com.example.cs551fitnessapp.ui.screens.WeightScreen
import java.lang.reflect.Modifier

enum class Screen {
    WORKOUT_PLAN,
    SEARCH_WORKOUT,
    WORKOUT_INFO,
    MEMBER_GOAL,
    MEMBER_MEDICAL,
    MEMBER_BIRTHDAY,
    MEMBER_HEIGHT,
    MEMBER_SEX,
    MEMBER_WEIGHT,

}

@Composable
fun AppNavGraph(
    onFlowComplete : (WorkoutPlanData) -> Unit = {},
    onFlowCancel   : () -> Unit                = {},
    startScreen : Screen = Screen.WORKOUT_PLAN, modifier: androidx.compose.ui.Modifier
) {
    val planViewModel : WorkoutPlanViewModel = viewModel()

    //  rememberSaveable — survives rotation
    var currentScreen by rememberSaveable {
        mutableStateOf(startScreen)
    }

    //  Store only the exercise ID
    var selectedExerciseId by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    // Resolve Exercise from ID using the search VM's last successful result
    val searchViewModel: com.example.cs551fitnessapp.ui.viewmodels.SearchWorkoutViewModel = viewModel()
    val currentExercise = remember(selectedExerciseId) {
        val state = searchViewModel.uiState.value
        if (state is com.example.cs551fitnessapp.ui.viewmodels.states.ExerciseUiState.Success) {
            state.exercises.find { it.id == selectedExerciseId }
        } else null
    }

    when (currentScreen) {

        Screen.WORKOUT_PLAN -> {
            WorkoutPlanScreen(
                planViewModel = planViewModel,
                onBackClick   = onFlowCancel,
                onAddWorkout  = { currentScreen = Screen.SEARCH_WORKOUT },
                onCancelClick = onFlowCancel,
                onDoneClick   = { data -> planViewModel.savePlan(data) },
                modifier = modifier
            )
        }

        Screen.SEARCH_WORKOUT -> {
            SearchWorkoutScreen(
                planViewModel  = planViewModel,
                searchViewModel = searchViewModel,
                onBackClick    = { currentScreen = Screen.WORKOUT_PLAN },
                onSaveClick    = { currentScreen = Screen.WORKOUT_PLAN },
                onAddExercise  = { exercise ->
                    selectedExerciseId = exercise.id
                    currentScreen      = Screen.WORKOUT_INFO
                },
                modifier = modifier
            )
        }

        Screen.WORKOUT_INFO -> {
            // Guard: if exercise lost fall back to search
            val exercise = currentExercise
            if (exercise == null) {
                currentScreen = Screen.SEARCH_WORKOUT
                return
            }
            WorkoutInfoScreen(
                exercise      = exercise,
                onBackClick   = { currentScreen = Screen.SEARCH_WORKOUT },
                onCancelClick = { currentScreen = Screen.SEARCH_WORKOUT },
                onAddClick    = { entry ->
                    planViewModel.addEntry(entry)
                    currentScreen = Screen.SEARCH_WORKOUT
                },
                modifier = modifier
            )
        }

        Screen.MEMBER_GOAL -> {
            MemberGoalScreen(onBackClick = {currentScreen = Screen.MEMBER_HEIGHT} ,
                onNextClick = {currentScreen = Screen.MEMBER_MEDICAL})
        }

        Screen.MEMBER_MEDICAL -> {
            MedicalConcernScreen(onBackClick = {currentScreen = Screen.MEMBER_GOAL})
        //Is this the final screen?
        }

        Screen.MEMBER_BIRTHDAY -> {
            BirthdayScreen(onBackClick = {currentScreen = Screen.MEMBER_SEX} ,
                onNextClick = {currentScreen = Screen.MEMBER_WEIGHT})
        }

        Screen.MEMBER_HEIGHT -> {
            HeightScreen(onBackClick = {currentScreen = Screen.MEMBER_WEIGHT} ,
                onNextClick = {currentScreen = Screen.MEMBER_GOAL})
        }

        Screen.MEMBER_SEX -> {
            NewMemberSexScreen(onBackClick = {/* go to members screen */} ,
                onNextClick = {currentScreen = Screen.MEMBER_BIRTHDAY})
        }

        Screen.MEMBER_WEIGHT -> {
            WeightScreen(onBackClick = {currentScreen = Screen.MEMBER_BIRTHDAY} ,
                onNextClick = {currentScreen = Screen.MEMBER_HEIGHT})
        }
    }
}