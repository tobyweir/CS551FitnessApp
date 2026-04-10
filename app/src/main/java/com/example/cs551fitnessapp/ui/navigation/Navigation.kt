package com.example.cs551fitnessapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
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
import com.example.cs551fitnessapp.ui.screens.SettingsScreen

import com.example.cs551fitnessapp.ui.screens.TodayScreen




@Composable
fun AppNavHost (navController : NavHostController , modifier : Modifier = Modifier) {
    NavHost(navController, startDestination = Today) {
        composable<Members> {
            MembersScreen(modifier = modifier)
        }
        composable<Today> {
            TodayScreen()
        }
        composable<PreferencesPage> {
            //Text(text = "preferences")
            SettingsScreen(onBack = { })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController : NavHostController , showBackIcon : Boolean ,  modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {Text(text = "Fitness App")},

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
fun BottomBar(navController : NavHostController, modifier: Modifier = Modifier) {
    val selectedNavigationIndex = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar() {
        NavigationBarItem(
            selected = selectedNavigationIndex.intValue == 0 ,
            onClick = {
                selectedNavigationIndex.intValue = 0
                navController.navigate(Today)
            },
            icon = { Icon(imageVector = Icons.Default.DateRange , contentDescription = "Today") },
            label = {}
        )
        NavigationBarItem(
            selected = selectedNavigationIndex.intValue == 1 ,
            onClick = {
                selectedNavigationIndex.intValue = 1
                navController.navigate(Members)
            },
            icon = { Icon(imageVector = Icons.Default.Person , contentDescription = "Members") },
            label = {}
        )
    }
}