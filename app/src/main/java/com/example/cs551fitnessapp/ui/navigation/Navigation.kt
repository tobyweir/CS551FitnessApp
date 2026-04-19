package com.example.cs551fitnessapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.cs551fitnessapp.ui.ViewModelFactory
import com.example.cs551fitnessapp.ui.screens.MemberInfoScreen
import com.example.cs551fitnessapp.ui.screens.MembersScreen
import com.example.cs551fitnessapp.ui.screens.SessionDetailScreen
import com.example.cs551fitnessapp.ui.screens.SettingsScreen
import com.example.cs551fitnessapp.ui.screens.TodayScreen
import com.example.cs551fitnessapp.ui.utils.WindowStateUtils
import com.example.cs551fitnessapp.ui.viewmodels.MembersViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    canGoBack: Boolean,
    navIndex: Int,
    updateIndex: (Int) -> Unit
) {
    val navigationType: WindowStateUtils =
        when (windowSize) {
            WindowWidthSizeClass.Compact -> WindowStateUtils.BOTTOM_NAVIGATION
            WindowWidthSizeClass.Medium -> WindowStateUtils.NAVIGATION_RAIL
            WindowWidthSizeClass.Expanded -> WindowStateUtils.NAVIGATION_RAIL
            else -> WindowStateUtils.BOTTOM_NAVIGATION
        }

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
                                title = "All Members"
                            )
                        }
                    ) { innerPadding ->
                        MembersScreen(
                            modifier = modifier.padding(innerPadding),
                            navController = navController,
                            viewmodel = membersViewModel,
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
                                title = "All Members"
                            )
                            Row {
                                SideBar(
                                    navController = navController,
                                    index = navIndex,
                                    updateIndex = updateIndex
                                )
                                MembersScreen(
                                    modifier = modifier.padding(innerPadding),
                                    navController = navController,
                                    viewmodel = membersViewModel,
                                    isWide = true
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
                                title = "Home"
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
                                title = "Home"
                            )
                            Row {
                                SideBar(
                                    navController = navController,
                                    index = navIndex,
                                    updateIndex = updateIndex
                                )
                                TodayScreen(
                                    navController = navController,
                                    modifier = modifier.padding(innerPadding),
                                    isWide = true
                                )
                            }
                        }
                    }
                }
            }
        }

        composable<PreferencesPage> {
            SettingsScreen(onBack = { navController.popBackStack() })
        }

        composable<MemberPage> { backStackEntry ->
            val member: MemberPage = backStackEntry.toRoute()

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

                WindowStateUtils.NAVIGATION_RAIL -> {
                    Scaffold(
                        modifier = modifier
                    ) { innerPadding ->
                        Column {
                            TopBar(
                                navController = navController,
                                showBackIcon = canGoBack,
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
                                    modifier = modifier.padding(innerPadding),
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }

        composable<SessionDetail> { backStackEntry ->
            val route: SessionDetail = backStackEntry.toRoute()

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
                                showBackIcon = canGoBack,
                                title = "Session"
                            )
                        }
                    ) { innerPadding ->
                        SessionDetailScreen(
                            sessionId = route.id,
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
                                showBackIcon = canGoBack,
                                title = "Session"
                            )
                            Row {
                                SideBar(
                                    navController = navController,
                                    index = navIndex,
                                    updateIndex = updateIndex
                                )
                                SessionDetailScreen(
                                    sessionId = route.id,
                                    modifier = modifier.padding(innerPadding)
                                )
                            }
                        }
                    }
                }
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
        title = {Text(text = title , color = MaterialTheme.colorScheme.primary)},

        navigationIcon = {BackNavigateIcon(navController = navController , showBackIcon = showBackIcon)},
        actions = {
            IconButton(onClick = { navController.navigate(PreferencesPage) }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings Icon",
                    tint = MaterialTheme.colorScheme.primary
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
                contentDescription = "Arrow Back Icon",
                tint = MaterialTheme.colorScheme.primary
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

@Composable
fun SideBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    index: Int,
    updateIndex: (Int) -> Unit
) {
    NavigationRail {
        NavigationRailItem(
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
        NavigationRailItem(
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