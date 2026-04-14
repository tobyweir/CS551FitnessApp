package com.example.cs551fitnessapp.ui.navigation

import android.app.Application
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cs551fitnessapp.database.WorkoutPlanData
import com.example.cs551fitnessapp.ui.screens.SearchWorkoutScreen
import com.example.cs551fitnessapp.ui.screens.WorkoutInfoScreen
import com.example.cs551fitnessapp.ui.screens.WorkoutPlanScreen
import com.example.cs551fitnessapp.ui.viewmodels.WorkoutPlanViewModel
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cs551fitnessapp.ui.screens.BirthdayScreen
import com.example.cs551fitnessapp.ui.screens.HeightScreen
import com.example.cs551fitnessapp.ui.screens.MedicalConcernScreen
import com.example.cs551fitnessapp.ui.screens.MemberGoalScreen
import com.example.cs551fitnessapp.ui.screens.NewMemberSexScreen
import com.example.cs551fitnessapp.ui.screens.WeightScreen


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
    startScreen : Screen = Screen.WORKOUT_PLAN, modifier: androidx.compose.ui.Modifier,
    navController : NavHostController
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
                onCancelClick = { navController.popBackStack() },
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
                onCancelClick = {navController.popBackStack()},
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
                onCancelClick = { navController.popBackStack() },
                onAddClick    = { entry ->
                    planViewModel.addEntry(entry)
                    currentScreen = Screen.SEARCH_WORKOUT
                },
                modifier = modifier
            )
        }

        Screen.MEMBER_GOAL -> {
            MemberGoalScreen(onBackClick = {currentScreen = Screen.MEMBER_HEIGHT} ,
                onNextClick = {currentScreen = Screen.MEMBER_MEDICAL} , modifier = modifier)
        }

        Screen.MEMBER_MEDICAL -> {
            MedicalConcernScreen(onBackClick = {currentScreen = Screen.MEMBER_GOAL}, modifier = modifier)
        //Is this the final screen?
        }

        Screen.MEMBER_BIRTHDAY -> {
            Scaffold(modifier = modifier, topBar = {
                GenericTopBar(onBackClick = {currentScreen = Screen.MEMBER_SEX})
            },) { innerPadding ->
                BirthdayScreen(onBackClick = { currentScreen = Screen.MEMBER_SEX },
                    onNextClick = { currentScreen = Screen.MEMBER_WEIGHT }, modifier = Modifier.padding(innerPadding))
            }
        }

        Screen.MEMBER_HEIGHT -> {
            Scaffold(modifier = modifier, topBar = {
                GenericTopBar(onBackClick = {currentScreen = Screen.MEMBER_WEIGHT})
            },) { innerPadding ->
                HeightScreen(onBackClick = { currentScreen = Screen.MEMBER_WEIGHT },
                    onNextClick = { currentScreen = Screen.MEMBER_GOAL }, modifier = Modifier.padding(innerPadding))
            }
        }

        Screen.MEMBER_SEX -> {
            Scaffold(modifier = modifier, topBar = {
                GenericTopBar(onBackClick = {navController.popBackStack()})
            },) { innerPadding ->
                NewMemberSexScreen(onBackClick = { navController.popBackStack() },
                    onNextClick = { currentScreen = Screen.MEMBER_BIRTHDAY }, modifier = Modifier.padding(innerPadding))
            }
        }

        Screen.MEMBER_WEIGHT -> {
            Scaffold(modifier = modifier, topBar = {
                GenericTopBar(onBackClick = {currentScreen = Screen.MEMBER_BIRTHDAY})
            },) { innerPadding ->
                WeightScreen(onBackClick = { currentScreen = Screen.MEMBER_BIRTHDAY },
                    onNextClick = { currentScreen = Screen.MEMBER_HEIGHT }, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}
private val PrimaryBlue = Color(0xFF2962FF)
private val LightGrayBg = Color(0xFFF5F5F5)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenericTopBar(
    //memberInitial : String,
    onBackClick   : () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text       = "Add Member",
                fontWeight = FontWeight.Bold,
                color      = PrimaryBlue,
                fontSize   = 18.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint               = PrimaryBlue
                )
            }
        },
        actions = { /* Set title in centre of screen */
            Spacer(modifier = androidx.compose.ui.Modifier.width(48.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}