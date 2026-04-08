package com.example.cs551fitnessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Scaffold

import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier

import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.example.cs551fitnessapp.ui.navigation.AppNavHost
import com.example.cs551fitnessapp.ui.navigation.BottomBar
import com.example.cs551fitnessapp.ui.theme.CS551FitnessAppTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            CS551FitnessAppTheme {

                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()

                val canGoBack =
                    navBackStackEntry != null &&
                            navController.previousBackStackEntry != null


                Scaffold(

                    modifier = Modifier.fillMaxSize(),

                    bottomBar = {

                        BottomBar(

                            navController = navController

                        )

                    }

                ) { innerPadding ->


                    AppNavHost(

                        navController = navController,

                        modifier = Modifier.padding(innerPadding)

                    )

                }
            }
        }
    }
}