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

private enum class Screen {
    WORKOUT_PLAN,
    SEARCH_WORKOUT,
    WORKOUT_INFO
}

@Composable
fun AppNavGraph(
    onFlowComplete : (WorkoutPlanData) -> Unit = {},
    onFlowCancel   : () -> Unit                = {}
) {
    val planViewModel : WorkoutPlanViewModel = viewModel()

    //  rememberSaveable — survives rotation
    var currentScreen by rememberSaveable {
        mutableStateOf(Screen.WORKOUT_PLAN)
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
                onDoneClick   = { data -> planViewModel.savePlan(data) }
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
                }
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
                }
            )
        }
    }
}