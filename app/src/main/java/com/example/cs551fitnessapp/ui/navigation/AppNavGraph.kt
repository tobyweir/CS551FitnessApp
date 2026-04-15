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

import com.example.cs551fitnessapp.ui.viewmodels.MembersViewModel
import com.example.cs551fitnessapp.ui.viewmodels.WorkoutPlanViewModel
import com.example.cs551fitnessapp.ui.viewmodels.SearchWorkoutViewModel
import com.example.cs551fitnessapp.ui.viewmodels.states.ExerciseUiState



enum class Screen {

    WORKOUT_PLAN,
    SEARCH_WORKOUT,
    WORKOUT_INFO,

    MEMBER_SEX,
    MEMBER_BIRTHDAY,
    MEMBER_WEIGHT,
    MEMBER_HEIGHT,
    MEMBER_GOAL,
    MEMBER_MEDICAL,

    MEMBER_NAME,
    MEMBER_SUCCESS

}



@Composable
fun AppNavGraph(

    navController: NavHostController,

    modifier: Modifier,

    membersViewModel: MembersViewModel,

    onFlowComplete: (WorkoutPlanData) -> Unit = {},

    onFlowCancel: () -> Unit = {},

    startScreen: Screen = Screen.WORKOUT_PLAN

) {

    val planViewModel: WorkoutPlanViewModel = viewModel()

    val searchViewModel: SearchWorkoutViewModel = viewModel()



    var currentScreen by rememberSaveable {

        mutableStateOf(startScreen)

    }



    var selectedExerciseId by rememberSaveable {

        mutableStateOf<String?>(null)

    }



    val currentExercise = remember(selectedExerciseId) {

        val state = searchViewModel.uiState.value

        if (state is ExerciseUiState.Success) {

            state.exercises.find {

                it.id == selectedExerciseId

            }

        } else null

    }



    when (currentScreen) {



        // ---------------- WORKOUT FLOW ----------------



        Screen.WORKOUT_PLAN -> {

            WorkoutPlanScreen(

                planViewModel = planViewModel,

                onBackClick = {

                    navController.popBackStack()

                },

                onAddWorkout = {

                    currentScreen = Screen.SEARCH_WORKOUT

                },

                onCancelClick = {

                    navController.popBackStack()

                },

                onDoneClick = { data ->

                    planViewModel.savePlan(data)

                },

                modifier = modifier

            )

        }



        Screen.SEARCH_WORKOUT -> {

            SearchWorkoutScreen(

                planViewModel = planViewModel,

                searchViewModel = searchViewModel,

                onBackClick = {

                    currentScreen = Screen.WORKOUT_PLAN

                },

                onSaveClick = {

                    currentScreen = Screen.WORKOUT_PLAN

                },

                onAddExercise = { exercise ->

                    selectedExerciseId = exercise.id

                    currentScreen = Screen.WORKOUT_INFO

                },

                onCancelClick = {

                    navController.popBackStack()

                },

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

                onBackClick = {

                    currentScreen = Screen.SEARCH_WORKOUT

                },

                onCancelClick = {

                    navController.popBackStack()

                },

                onAddClick = { entry ->

                    planViewModel.addEntry(entry)

                    currentScreen = Screen.SEARCH_WORKOUT

                },

                modifier = modifier

            )

        }



        // ---------------- ADD MEMBER FLOW ----------------



        Screen.MEMBER_SEX -> {

            Scaffold(

                topBar = {

                    GenericTopBar {

                        navController.popBackStack()

                    }

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

                topBar = {

                    GenericTopBar {

                        currentScreen = Screen.MEMBER_SEX

                    }

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

                topBar = {

                    GenericTopBar {

                        currentScreen = Screen.MEMBER_BIRTHDAY

                    }

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

                topBar = {

                    GenericTopBar {

                        currentScreen = Screen.MEMBER_WEIGHT

                    }

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

                onSaveClick = { name, sessions ->



                    membersViewModel.addMember(

                        name = name,

                        sessions = sessions

                    )



                    currentScreen = Screen.MEMBER_SUCCESS

                },

                modifier = modifier

            )

        }



        Screen.MEMBER_SUCCESS -> {

            MemberSuccessScreen(

                onFinished = {

                    navController.popBackStack()

                }

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

            IconButton(onClick = onBackClick) {

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