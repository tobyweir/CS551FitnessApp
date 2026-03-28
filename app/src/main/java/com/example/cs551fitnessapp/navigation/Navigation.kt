package com.example.cs551fitnessapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
                name = "Android",
                modifier = Modifier.padding()
            )
        }
        composable<Today> {
            Greeting(
                name = "Android",
                modifier = Modifier.padding()
            )
        }
    }
}