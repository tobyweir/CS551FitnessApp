package com.example.cs551fitnessapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
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
import androidx.lifecycle.ViewModelProvider

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




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost (navController : NavHostController , modifier : Modifier = Modifier , canGoBack : Boolean) {
    NavHost(navController, startDestination = Today) {

        composable<Members> {
            Scaffold(modifier = modifier, bottomBar = {BottomBar(navController = navController)},
                topBar = {TopBar(navController = navController, showBackIcon =  canGoBack , title = "Members")}
            ) { innerPadding ->
                MembersScreen(modifier = modifier.padding(innerPadding), navController = navController)
            }
        }

        composable<Today> {
            Scaffold(modifier = modifier, bottomBar = {BottomBar(navController = navController)},
                topBar = {TopBar(navController = navController, showBackIcon =  canGoBack, title = "Today")}
            ) { innerPadding ->
                TodayScreen(navController = navController, modifier = modifier.padding(innerPadding))
            }
        }

        composable<PreferencesPage> {
            //Text(text = "preferences")
                SettingsScreen(onBack = { navController.popBackStack()} , modifier = modifier)

        }

        composable<MemberPage> { backStackEntry ->
            val member : MemberPage = backStackEntry.toRoute()
            Scaffold(modifier = modifier, bottomBar = {BottomBar(navController = navController)},
                topBar = {TopBar(navController = navController, showBackIcon =  canGoBack , title = "Member")}
            ) { innerPadding ->
                MemberInfoScreen(member.id, modifier = modifier.padding(innerPadding), navController = navController)
            }
        }

        composable<AddMemberFlow> {
            AppNavGraph (startScreen = Screen.MEMBER_SEX , modifier = modifier , navController = navController)

        }

        composable<AddWorkoutFlow> {  backStackEntry ->
            val member : AddWorkoutFlow = backStackEntry.toRoute()
            AppNavGraph (startScreen = Screen.WORKOUT_PLAN , modifier = modifier, navController = navController)

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