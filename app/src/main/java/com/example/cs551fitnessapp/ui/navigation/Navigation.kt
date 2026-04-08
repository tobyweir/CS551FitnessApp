package com.example.cs551fitnessapp.ui.navigation

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

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.cs551fitnessapp.ui.today.TodayScreen


/*
NAVIGATION HOST
*/

@Composable
fun AppNavHost(

    navController: NavHostController,

    modifier: Modifier = Modifier

) {

    NavHost(

        navController = navController,

        startDestination = Today,

        modifier = modifier

    ) {

        composable<Today> {

            TodayScreen()

        }

        composable<Members> {

            Text("Members screen")

        }

        composable<PreferencesPage> {

            Text("Settings screen")

        }
    }
}



/*
TOP BAR
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(

    navController: NavHostController,

    showBackIcon: Boolean

) {

    CenterAlignedTopAppBar(

        title = {

            Text("Fitness App")

        },

        navigationIcon = {

            if (showBackIcon) {

                IconButton(

                    onClick = {

                        navController.popBackStack()

                    }

                ) {

                    Icon(

                        Icons.AutoMirrored.Filled.ArrowBack,

                        contentDescription = null

                    )
                }
            }
        },

        actions = {

            IconButton(

                onClick = {

                    navController.navigate(PreferencesPage)

                }

            ) {

                Icon(

                    Icons.Default.Settings,

                    contentDescription = null

                )
            }
        }
    )
}



/*
BOTTOM BAR
*/

@Composable
fun BottomBar(

    navController: NavHostController

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

                    contentDescription = null

                )
            },

            label = {

                Text("Today")

            }
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

                    contentDescription = null

                )
            },

            label = {

                Text("Members")

            }
        )
    }
}