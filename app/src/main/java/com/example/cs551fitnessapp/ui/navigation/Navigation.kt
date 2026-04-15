package com.example.cs551fitnessapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable

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

    canGoBack: Boolean

) {

    // ONE shared ViewModel
    val membersViewModel: MembersViewModel =
        viewModel(factory = ViewModelFactory.Factory)



    NavHost(

        navController = navController,

        startDestination = Today

    ) {

        // ---------------- MEMBERS ----------------

        composable<Members> {

            Scaffold(

                modifier = modifier,

                bottomBar = {

                    BottomBar(navController)

                },

                topBar = {

                    TopBar(

                        navController = navController,

                        showBackIcon = canGoBack,

                        title = "Members"

                    )

                }

            ) { padding ->



                MembersScreen(

                    navController = navController,

                    modifier = modifier.padding(padding),

                    viewmodel = membersViewModel   // SAME INSTANCE

                )

            }

        }



        // ---------------- TODAY ----------------

        composable<Today> {

            Scaffold(

                modifier = modifier,

                bottomBar = {

                    BottomBar(navController)

                },

                topBar = {

                    TopBar(

                        navController = navController,

                        showBackIcon = canGoBack,

                        title = "Today"

                    )

                }

            ) { padding ->



                TodayScreen(

                    navController = navController,

                    modifier = modifier.padding(padding)

                )

            }

        }



        // ---------------- SETTINGS ----------------

        composable<PreferencesPage> {

            SettingsScreen(

                onBack = {

                    navController.popBackStack()

                }

            )

        }



        // ---------------- MEMBER DETAILS ----------------

        composable<MemberPage> { backStackEntry ->

            val member: MemberPage = backStackEntry.toRoute()



            Scaffold(

                modifier = modifier,

                bottomBar = {

                    BottomBar(navController)

                },

                topBar = {

                    TopBar(

                        navController = navController,

                        showBackIcon = canGoBack,

                        title = "Member"

                    )

                }

            ) { padding ->



                MemberInfoScreen(

                    member.id,

                    modifier = modifier.padding(padding),

                    navController = navController

                )

            }

        }



        // ---------------- ADD MEMBER FLOW ----------------

        composable<AddMemberFlow> {

            AppNavGraph(

                navController = navController,

                modifier = modifier,

                startScreen = Screen.MEMBER_SEX,

                membersViewModel = membersViewModel   // SAME INSTANCE

            )

        }



        // ---------------- ADD WORKOUT FLOW ----------------

        composable<AddWorkoutFlow> { backStackEntry ->

            val member: AddWorkoutFlow = backStackEntry.toRoute()



            AppNavGraph(

                navController = navController,

                modifier = modifier,

                startScreen = Screen.WORKOUT_PLAN,

                membersViewModel = membersViewModel

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

        title = {

            Text(title)

        },

        navigationIcon = {

            BackNavigateIcon(

                navController,

                showBackIcon

            )

        },

        actions = {

            IconButton(

                onClick = {

                    navController.navigate(

                        PreferencesPage

                    )

                }

            ) {

                Icon(

                    imageVector = Icons.Default.Settings,

                    contentDescription = "Settings"

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

        IconButton(

            onClick = {

                navController.popBackStack()

            }

        ) {

            Icon(

                imageVector = Icons.AutoMirrored.Filled.ArrowBack,

                contentDescription = "Back"

            )

        }

    }

}



@Composable
fun BottomBar(

    navController: NavHostController,

    modifier: Modifier = Modifier

) {

    val selectedIndex = rememberSaveable {

        mutableIntStateOf(0)

    }



    NavigationBar {

        NavigationBarItem(

            selected = selectedIndex.intValue == 0,

            onClick = {

                selectedIndex.intValue = 0

                navController.navigate(Today)

            },

            icon = {

                Icon(

                    Icons.Default.DateRange,

                    contentDescription = "Today"

                )

            },

            label = {}

        )



        NavigationBarItem(

            selected = selectedIndex.intValue == 1,

            onClick = {

                selectedIndex.intValue = 1

                navController.navigate(Members)

            },

            icon = {

                Icon(

                    Icons.Default.Person,

                    contentDescription = "Members"

                )

            },

            label = {}

        )

    }

}