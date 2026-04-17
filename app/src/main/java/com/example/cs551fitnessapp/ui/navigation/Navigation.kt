package com.example.cs551fitnessapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.cs551fitnessapp.ui.ViewModelFactory
import com.example.cs551fitnessapp.ui.screens.MemberInfoScreen
import com.example.cs551fitnessapp.ui.screens.MembersScreen
import com.example.cs551fitnessapp.ui.screens.SettingsScreen
import com.example.cs551fitnessapp.ui.screens.TodayScreen
import com.example.cs551fitnessapp.ui.viewmodels.MembersViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    canGoBack: Boolean,
    navIndex: Int,
    updateIndex: (Int) -> Unit
) {
    val membersViewModel: MembersViewModel =
        viewModel(factory = ViewModelFactory.Factory)

    NavHost(navController, startDestination = Today) {
        composable<Members> {
            Scaffold(
                modifier = modifier,
                bottomBar = {
                    BottomBar(
                        navController = navController,
                        index = navIndex,
                        updateIndex = updateIndex
                    )
                },
                topBar = {
                    TopBar(
                        navController = navController,
                        showBackIcon = false,
                        title = "Members"
                    )
                }
            ) { innerPadding ->
                MembersScreen(
                    modifier = modifier.padding(innerPadding),
                    navController = navController,
                    viewmodel = membersViewModel
                )
            }
        }

        composable<Today> {
            Scaffold(
                modifier = modifier,
                bottomBar = {
                    BottomBar(
                        navController = navController,
                        index = navIndex,
                        updateIndex = updateIndex
                    )
                },
                topBar = {
                    TopBar(
                        navController = navController,
                        showBackIcon = false,
                        title = "Today"
                    )
                }
            ) { innerPadding ->
                TodayScreen(
                    navController = navController,
                    modifier = modifier.padding(innerPadding)
                )
            }
        }

        composable<PreferencesPage> {
            SettingsScreen(onBack = { navController.popBackStack() })
        }

        composable<MemberPage> { backStackEntry ->
            val member: MemberPage = backStackEntry.toRoute()
            Scaffold(
                modifier = modifier,
                bottomBar = {
                    BottomBar(
                        navController = navController,
                        index = navIndex,
                        updateIndex = updateIndex
                    )
                },
                topBar = {
                    TopBar(
                        navController = navController,
                        showBackIcon = canGoBack,
                        title = "Member"
                    )
                }
            ) { innerPadding ->
                MemberInfoScreen(
                    member.id,
                    modifier = modifier.padding(innerPadding),
                    navController = navController
                )
            }
        }

        composable<AddMemberFlow> {
            AppNavGraph(
                startScreen = Screen.MEMBER_SEX,
                modifier = modifier,
                navController = navController,
                membersViewModel = membersViewModel
            )
        }

        composable<AddWorkoutFlow> { backStackEntry ->
            val flow: AddWorkoutFlow = backStackEntry.toRoute()
            AppNavGraph(
                startScreen = Screen.WORKOUT_PLAN,
                modifier = modifier,
                navController = navController,
                membersViewModel = membersViewModel,
                memberId = flow.id.toLong()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavHostController,
    showBackIcon: Boolean,
    modifier: Modifier = Modifier,
    title: String = "Fitness App"
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            BackNavigateIcon(
                navController = navController,
                showBackIcon = showBackIcon
            )
        },
        actions = {
            IconButton(onClick = { navController.navigate(PreferencesPage) }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings Icon"
                )
            }
        }
    )
}

@Composable
fun BackNavigateIcon(
    navController: NavHostController,
    showBackIcon: Boolean,
    modifier: Modifier = Modifier
) {
    if (showBackIcon) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Arrow Back Icon"
            )
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    index: Int,
    updateIndex: (Int) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = index == 0,
            onClick = {
                updateIndex(0)
                navController.navigate(Today)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Today"
                )
            },
            label = {}
        )
        NavigationBarItem(
            selected = index == 1,
            onClick = {
                updateIndex(1)
                navController.navigate(Members)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Members"
                )
            },
            label = {}
        )
    }
}