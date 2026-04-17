package com.example.cs551fitnessapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cs551fitnessapp.database.WorkoutPlanData
import com.example.cs551fitnessapp.ui.ViewModelFactory
import com.example.cs551fitnessapp.ui.screens.AddMemberNameScreen
import com.example.cs551fitnessapp.ui.screens.BirthdayScreen
import com.example.cs551fitnessapp.ui.screens.HeightScreen
import com.example.cs551fitnessapp.ui.screens.MedicalConcernScreen
import com.example.cs551fitnessapp.ui.screens.MemberGoalScreen
import com.example.cs551fitnessapp.ui.screens.MemberSuccessScreen
import com.example.cs551fitnessapp.ui.screens.NewMemberSexScreen
import com.example.cs551fitnessapp.ui.screens.SearchWorkoutScreen
import com.example.cs551fitnessapp.ui.screens.WeightScreen
import com.example.cs551fitnessapp.ui.screens.WorkoutInfoScreen
import com.example.cs551fitnessapp.ui.screens.WorkoutPlanScreen
import com.example.cs551fitnessapp.ui.viewmodels.AddMemberViewModel
import com.example.cs551fitnessapp.ui.viewmodels.MembersViewModel
import com.example.cs551fitnessapp.ui.viewmodels.SearchWorkoutViewModel
import com.example.cs551fitnessapp.ui.viewmodels.WorkoutPlanViewModel
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier,
    membersViewModel: MembersViewModel,
    memberId: Long? = null,
    onFlowComplete: (WorkoutPlanData) -> Unit = {},
    onFlowCancel: () -> Unit = {},
    startScreen: Screen = Screen.WORKOUT_PLAN
) {
    val planViewModel: WorkoutPlanViewModel = viewModel(factory = ViewModelFactory.Factory)
    val addMemberViewModel: AddMemberViewModel = viewModel(factory = ViewModelFactory.Factory)
    val searchViewModel: SearchWorkoutViewModel = viewModel()

    LaunchedEffect(memberId) {
        if (memberId != null) {
            planViewModel.setSelectedMemberId(memberId)
        }
    }

    var currentScreen by rememberSaveable {
        mutableStateOf(startScreen)
    }

    var selectedExerciseId by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val searchUiState by searchViewModel.uiState.collectAsState()

    val currentExercise = remember(selectedExerciseId, searchUiState) {
        val state = searchUiState
        if (state is ExerciseUiState.Success) {
            state.exercises.find { it.id == selectedExerciseId }
        } else {
            null
        }
    }

    when (currentScreen) {
        Screen.WORKOUT_PLAN -> {
            WorkoutPlanScreen(
                planViewModel = planViewModel,
                onBackClick = { navController.popBackStack() },
                onAddWorkout = { currentScreen = Screen.SEARCH_WORKOUT },
                onCancelClick = {
                    onFlowCancel()
                    navController.popBackStack()
                },
                onDoneClick = { data ->
                    planViewModel.savePlan(data)
                    onFlowComplete(data)
                },
                onNavigateToMemberInfo = {
                    val targetId = memberId?.toInt() ?: 1
                    navController.navigate(MemberPage(id = targetId)) {
                        popUpTo<AddWorkoutFlow> { inclusive = true }
                    }
                },
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
                onCancelClick = {
                    onFlowCancel()
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
                onBackClick = { currentScreen = Screen.SEARCH_WORKOUT },
                onCancelClick = {
                    onFlowCancel()
                    navController.popBackStack()
                },
                onAddClick = { entry ->
                    planViewModel.addEntry(entry)
                    currentScreen = Screen.SEARCH_WORKOUT
                },
                modifier = modifier
            )
        }

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
                    onNextClick = { selectedSex ->
                        addMemberViewModel.updateSex(selectedSex)
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
                    birthday = addMemberViewModel.birthday,
                    onBirthdayChange = { addMemberViewModel.updateBirthday(it) },
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
                    weight = addMemberViewModel.weight,
                    weightUnit = addMemberViewModel.weightUnit,
                    onWeightChange = { addMemberViewModel.updateWeight(it) },
                    onWeightUnitChange = { addMemberViewModel.updateWeightUnit(it) },
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
                    height = addMemberViewModel.height,
                    heightUnit = addMemberViewModel.heightUnit,
                    onHeightChange = { addMemberViewModel.updateHeight(it) },
                    onHeightUnitChange = { addMemberViewModel.updateHeightUnit(it) },
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
                selectedGoalId = addMemberViewModel.goalId,
                onGoalSelected = { selectedId ->
                    addMemberViewModel.updateGoalSelection(selectedId)
                },
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
                selectedConcernId = addMemberViewModel.medicalConcernId,
                note = addMemberViewModel.medicalNote,
                onConcernSelected = { selectedId ->
                    addMemberViewModel.updateMedicalConcernSelection(selectedId)
                },
                onNoteChange = { addMemberViewModel.updateMedicalNote(it) },
                onBackClick = {
                    currentScreen = Screen.MEMBER_GOAL
                },
                onNextClick = {
                    currentScreen = Screen.MEMBER_NAME
                },
                modifier = modifier
            )
        }

        Screen.MEMBER_NAME -> {
            Scaffold(topBar = {GenericTopBar{currentScreen= Screen.MEMBER_MEDICAL}}) { innerPadding ->
                AddMemberNameScreen(
                    name = addMemberViewModel.name,
                    sessions = addMemberViewModel.sessionsInput,
                    onNameChange = { addMemberViewModel.updateName(it) },
                    onSessionsChange = { addMemberViewModel.updateSessionsInput(it) },
                    onBackClick = {
                        currentScreen = Screen.MEMBER_MEDICAL
                    },
                    onSaveClick = {
                        addMemberViewModel.saveMember {
                            currentScreen = Screen.MEMBER_SUCCESS
                        }
                    },
                    modifier = modifier.padding(innerPadding)
                )
            }
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
                text = "Add Member",
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.primary,
                fontSize   = 18.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint               = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = {
            Spacer(modifier = Modifier.width(48.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}