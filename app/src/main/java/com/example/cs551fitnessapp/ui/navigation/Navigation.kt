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

import com.example.cs551fitnessapp.ui.screens.MembersScreen
import com.example.cs551fitnessapp.ui.screens.TodayScreen
import com.example.cs551fitnessapp.ui.screens.MemberInfoScreen

import com.example.cs551fitnessapp.ui.viewmodels.states.MemberUiState



@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = "today"
    ) {

        composable("today") {
            TodayScreen(navController)
        }

        composable("members") {
            MembersScreen(modifier = modifier)
        }

        composable("preferences") {
            Text("preferences")
        }

        composable(
            route = "member_info/{name}"
        ) { backStackEntry ->

            val name =
                backStackEntry.arguments
                    ?.getString("name")
                    ?: ""

            MemberInfoScreen(
                member = MemberUiState(name = name)
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavHostController,
    showBackIcon: Boolean,
    modifier: Modifier = Modifier
) {

    CenterAlignedTopAppBar(

        title = {
            Text("Fitness App")
        },

        navigationIcon = {
            BackNavigateIcon(navController, showBackIcon)
        },

        actions = {

            IconButton(

                onClick = {
                    navController.navigate("preferences")
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
    showBackIcon: Boolean
) {

    if (showBackIcon) {

        IconButton(
            onClick = { navController.popBackStack() }
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
    navController: NavHostController
) {

    val selectedNavigationIndex =
        rememberSaveable {
            mutableIntStateOf(0)
        }

    NavigationBar {

        NavigationBarItem(

            selected =
                selectedNavigationIndex.intValue == 0,

            onClick = {

                selectedNavigationIndex.intValue = 0

                navController.navigate("today")
            },

            icon = {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Today"
                )
            }
        )



        NavigationBarItem(

            selected =
                selectedNavigationIndex.intValue == 1,

            onClick = {

                selectedNavigationIndex.intValue = 1

                navController.navigate("members")
            },

            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Members"
                )
            }
        )
    }
}