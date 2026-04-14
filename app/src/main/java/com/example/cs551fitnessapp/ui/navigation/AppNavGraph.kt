package com.example.cs551fitnessapp.ui.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

import com.example.cs551fitnessapp.database.WorkoutPlanData

import com.example.cs551fitnessapp.ui.screens.*

import com.example.cs551fitnessapp.ui.viewmodels.WorkoutPlanViewModel



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

    MEMBER_NAME   // final screen
}



@Composable
fun AppNavGraph(

    navController: NavHostController,

    modifier: Modifier,

    onFlowComplete: (WorkoutPlanData) -> Unit = {},

    onFlowCancel: () -> Unit = {},

    startScreen: Screen = Screen.WORKOUT_PLAN

) {

    val planViewModel: WorkoutPlanViewModel = viewModel()


    var currentScreen by rememberSaveable {

        mutableStateOf(startScreen)

    }


    var selectedExerciseId by rememberSaveable {

        mutableStateOf<String?>(null)

    }



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

                onBackClick = { navController.popBackStack() },

                onAddWorkout = { currentScreen = Screen.SEARCH_WORKOUT },

                onCancelClick = { navController.popBackStack() },

                onDoneClick = { data -> planViewModel.savePlan(data) },

                modifier = modifier

            )

        }



        Screen.SEARCH_WORKOUT -> {

            SearchWorkoutScreen(

                planViewModel = planViewModel,

                searchViewModel = searchViewModel,

                onBackClick = { currentScreen = Screen.WORKOUT_PLAN },

                onSaveClick = { currentScreen = Screen.WORKOUT_PLAN },

                onAddExercise = { exercise ->

                    selectedExerciseId = exercise.id

                    currentScreen = Screen.WORKOUT_INFO

                },

                onCancelClick = { navController.popBackStack() },

                modifier = modifier

            )

        }



        Screen.WORKOUT_INFO -> {

            val exercise = currentExercise

            if (exercise == null) {

                currentScreen = Screen.SEARCH_WORKOUT

                return

            }



            WorkoutInfoScreen(

                exercise = exercise,

                onBackClick = { currentScreen = Screen.SEARCH_WORKOUT },

                onCancelClick = { navController.popBackStack() },

                onAddClick = {

                    planViewModel.addEntry(it)

                    currentScreen = Screen.SEARCH_WORKOUT

                },

                modifier = modifier

            )

        }



        // ---------- ADD MEMBER FLOW ----------



        Screen.MEMBER_SEX -> {

            Scaffold(

                modifier = modifier,

                topBar = {

                    GenericTopBar(

                        onBackClick = {

                            navController.popBackStack()

                        }

                    )

                }

            ) { padding ->



                NewMemberSexScreen(

                    onBackClick = {

                        navController.popBackStack()

                    },

                    onNextClick = {

                        currentScreen = Screen.MEMBER_BIRTHDAY

                    },

                    modifier = Modifier.padding(padding)

                )

            }

        }



        Screen.MEMBER_BIRTHDAY -> {

            Scaffold(

                modifier = modifier,

                topBar = {

                    GenericTopBar(

                        onBackClick = {

                            currentScreen = Screen.MEMBER_SEX

                        }

                    )

                }

            ) { padding ->



                BirthdayScreen(

                    onBackClick = {

                        currentScreen = Screen.MEMBER_SEX

                    },

                    onNextClick = {

                        currentScreen = Screen.MEMBER_WEIGHT

                    },

                    modifier = Modifier.padding(padding)

                )

            }

        }



        Screen.MEMBER_WEIGHT -> {

            Scaffold(

                modifier = modifier,

                topBar = {

                    GenericTopBar(

                        onBackClick = {

                            currentScreen = Screen.MEMBER_BIRTHDAY

                        }

                    )

                }

            ) { padding ->



                WeightScreen(

                    onBackClick = {

                        currentScreen = Screen.MEMBER_BIRTHDAY

                    },

                    onNextClick = {

                        currentScreen = Screen.MEMBER_HEIGHT

                    },

                    modifier = Modifier.padding(padding)

                )

            }

        }



        Screen.MEMBER_HEIGHT -> {

            Scaffold(

                modifier = modifier,

                topBar = {

                    GenericTopBar(

                        onBackClick = {

                            currentScreen = Screen.MEMBER_WEIGHT

                        }

                    )

                }

            ) { padding ->



                HeightScreen(

                    onBackClick = {

                        currentScreen = Screen.MEMBER_WEIGHT

                    },

                    onNextClick = {

                        currentScreen = Screen.MEMBER_GOAL

                    },

                    modifier = Modifier.padding(padding)

                )

            }

        }



        Screen.MEMBER_GOAL -> {

            MemberGoalScreen(

                onBackClick = {

                    currentScreen = Screen.MEMBER_HEIGHT

                },

                onNextClick = {

                    currentScreen = Screen.MEMBER_MEDICAL

                },

                modifier = modifier

            )

        }



        Screen.MEMBER_MEDICAL -> {

            MedicalConcernScreen(

                onBackClick = {

                    currentScreen = Screen.MEMBER_GOAL

                },

                onNextClick = { _, _ ->

                    currentScreen = Screen.MEMBER_NAME

                },

                modifier = modifier

            )

        }



        Screen.MEMBER_NAME -> {

            AddMemberNameScreen(

                onBackClick = {

                    currentScreen = Screen.MEMBER_MEDICAL

                },

                onSaveClick = {

                    navController.popBackStack()

                },

                modifier = modifier

            )

        }

    }

}



private val PrimaryBlue = Color(0xFF2962FF)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenericTopBar(

    onBackClick: () -> Unit

) {

    CenterAlignedTopAppBar(

        title = {

            Text(

                "Add Member",

                fontWeight = FontWeight.Bold,

                color = PrimaryBlue,

                fontSize = 18.sp

            )

        },

        navigationIcon = {

            IconButton(

                onClick = onBackClick

            ) {

                Icon(

                    Icons.AutoMirrored.Filled.ArrowBack,

                    contentDescription = "Back",

                    tint = PrimaryBlue

                )

            }

        },

        actions = {

            Spacer(

                modifier = Modifier.width(48.dp)

            )

        },

        colors = TopAppBarDefaults.topAppBarColors(

            containerColor = Color.White

        )

    )

}