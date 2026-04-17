package com.example.cs551fitnessapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings

import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.cs551fitnessapp.ui.ViewModelFactory
import com.example.cs551fitnessapp.ui.screens.MemberInfoScreen
import com.example.cs551fitnessapp.ui.screens.MembersScreen
import com.example.cs551fitnessapp.ui.screens.SearchWorkoutScreen
import com.example.cs551fitnessapp.ui.screens.SettingsScreen

import com.example.cs551fitnessapp.ui.screens.TodayScreen
import com.example.cs551fitnessapp.ui.utils.WindowStateUtils
import com.example.cs551fitnessapp.ui.viewmodels.MembersViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost (navController : NavHostController ,
                windowSize: WindowWidthSizeClass,
                modifier : Modifier = Modifier ,
                canGoBack : Boolean , navIndex : Int,
                updateIndex: (Int) -> Unit) {

    val navigationType: WindowStateUtils
    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = WindowStateUtils.BOTTOM_NAVIGATION

        }

        WindowWidthSizeClass.Medium -> {
            navigationType = WindowStateUtils.NAVIGATION_RAIL

        }

        WindowWidthSizeClass.Expanded -> {
            navigationType = WindowStateUtils.NAVIGATION_RAIL

        }

        else -> {
            navigationType = WindowStateUtils.BOTTOM_NAVIGATION

        }
    }
    // ONE shared ViewModel
    val membersViewModel: MembersViewModel =
        viewModel(factory = ViewModelFactory.Factory)
    NavHost(navController, startDestination = Today) {
        composable<Members> {
            when (navigationType) {
                WindowStateUtils.BOTTOM_NAVIGATION -> {
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
                            navController = navController,
                            modifier = modifier.padding(innerPadding)
                        )
                    }

                }

                WindowStateUtils.NAVIGATION_RAIL -> {
                    Scaffold(
                        modifier = modifier
                    ) { innerPadding ->
                        Column {
                            TopBar(
                                navController = navController,
                                showBackIcon = false,
                                title = "Members"
                            )
                            Row {
                                SideBar(
                                    navController = navController,
                                    index = navIndex,
                                    updateIndex = updateIndex
                                )
                                MembersScreen(
                                    navController = navController,
                                    modifier = modifier
                                        .padding(innerPadding)
                                )
                            }
                        }


                    }

                }


            }
        }

        composable<Today> {

            when (navigationType) {
                WindowStateUtils.BOTTOM_NAVIGATION -> {
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

                WindowStateUtils.NAVIGATION_RAIL -> {
                    Scaffold(
                        modifier = modifier
                    ) { innerPadding ->
                        Column {
                            TopBar(
                                navController = navController,
                                showBackIcon = false,
                                title = "Today"
                            )
                            Row {
                                SideBar(
                                    navController = navController,
                                    index = navIndex,
                                    updateIndex = updateIndex
                                )
                                TodayScreen(
                                    navController = navController,
                                    modifier = modifier
                                        .padding(innerPadding)
                                )
                            }
                        }


                    }

                }


        }




        }

        composable<PreferencesPage> {
            //Text(text = "preferences")
                SettingsScreen(onBack = { navController.popBackStack()})

        }

        composable<MemberPage> { backStackEntry ->
            val member : MemberPage = backStackEntry.toRoute()
            when (navigationType) {
                WindowStateUtils.BOTTOM_NAVIGATION -> {
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
                                title = "Member"
                            )
                        }
                    ) { innerPadding ->
                        MemberInfoScreen(
                            member.id,
                            navController = navController,
                            modifier = modifier.padding(innerPadding)
                        )
                    }

                }

                WindowStateUtils.NAVIGATION_RAIL -> {
                    Scaffold(
                        modifier = modifier
                    ) { innerPadding ->
                        Column {
                            TopBar(
                                navController = navController,
                                showBackIcon = false,
                                title = "Member"
                            )
                            Row {
                                SideBar(
                                    navController = navController,
                                    index = navIndex,
                                    updateIndex = updateIndex
                                )
                                MemberInfoScreen(
                                    member.id,
                                    navController = navController,
                                    modifier = modifier
                                        .padding(innerPadding)
                                )
                            }
                        }


                    }

                }


            }
        }

        composable<AddMemberFlow> {
            AppNavGraph (startScreen = Screen.MEMBER_SEX , modifier = modifier , navController = navController , membersViewModel = membersViewModel)

        }

        composable<AddWorkoutFlow> {  backStackEntry ->
            val member : AddWorkoutFlow = backStackEntry.toRoute()
            AppNavGraph (startScreen = Screen.WORKOUT_PLAN , modifier = modifier, navController = navController , membersViewModel = membersViewModel)

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController : NavHostController , showBackIcon : Boolean ,  modifier: Modifier = Modifier , title : String = "Fitness App") {
    CenterAlignedTopAppBar(
        title = {Text(text = title)},

        navigationIcon = {BackNavigateIcon(navController = navController , showBackIcon = showBackIcon)},
        actions = {
            IconButton(onClick = {navController.navigate(PreferencesPage)}) {
                Icon (
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings Icon"
                )
            }
        }
    )
}

@Composable
fun BackNavigateIcon (navController: NavHostController , showBackIcon : Boolean , modifier: Modifier = Modifier) {
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
fun BottomBar(navController : NavHostController, modifier: Modifier = Modifier , index : Int , updateIndex : (Int) -> Unit) {


    NavigationBar() {
        NavigationBarItem(
            selected = index == 0 ,
            onClick = {
                updateIndex(0)
                navController.navigate(Today)
            },
            icon = { Icon(imageVector = Icons.Default.DateRange , contentDescription = "Today") },
            label = {}
        )
        NavigationBarItem(
            selected = index == 1 ,
            onClick = {
                updateIndex(1)
                navController.navigate(Members)
            },
            icon = { Icon(imageVector = Icons.Default.Person , contentDescription = "Members") },
            label = {}
        )
    }
}

@Composable
fun SideBar(navController : NavHostController, modifier: Modifier = Modifier , index : Int , updateIndex : (Int) -> Unit) {


    NavigationRail () {
        NavigationRailItem(
            selected = index == 0 ,
            onClick = {
                updateIndex(0)
                navController.navigate(Today)
            },
            icon = { Icon(imageVector = Icons.Default.DateRange , contentDescription = "Today") },
            label = {}
        )
        NavigationRailItem(
            selected = index == 1 ,
            onClick = {
                updateIndex(1)
                navController.navigate(Members)
            },
            icon = { Icon(imageVector = Icons.Default.Person , contentDescription = "Members") },
            label = {}
        )
    }
}