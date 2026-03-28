package com.example.cs551fitnessapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cs551fitnessapp.Greeting

@Composable
fun AppNavHost (navController : NavHostController , modifier : Modifier = Modifier) {
    NavHost(navController, startDestination = Members) {
        composable<Members> {
            Greeting(
                name = "Members screen",
                modifier = modifier
            )
        }
        composable<Today> {
            Greeting(
                name = "today screen",
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController : NavHostController , showBackIcon : Boolean ,  modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {Text(text = "Fitness App")},

        navigationIcon = {BackNavigateIcon(navController = navController , showBackIcon = showBackIcon)},

        )
}

@Composable
fun BackNavigateIcon (navController: NavHostController , showBackIcon : Boolean , modifier: Modifier = Modifier) {
    if (showBackIcon) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Localized description"
            )
        }
    }

}


@Composable
fun BottomBar(navController : NavHostController, modifier: Modifier = Modifier) {
    val selectedNavigationIndex = rememberSaveable {
        mutableIntStateOf(1)
    }

    NavigationBar() {
        NavigationBarItem(
            selected = selectedNavigationIndex.intValue == 0 ,
            onClick = {
                selectedNavigationIndex.intValue = 0
                navController.navigate(Today)
            },
            icon = { Icon(imageVector = Icons.Default.List , contentDescription = "Today") },
            label = {}
        )
        NavigationBarItem(
            selected = selectedNavigationIndex.intValue == 1 ,
            onClick = {
                selectedNavigationIndex.intValue = 1
                navController.navigate(Members)
            },
            icon = { Icon(imageVector = Icons.Default.Home , contentDescription = "Members") },
            label = {}
        )
    }
}