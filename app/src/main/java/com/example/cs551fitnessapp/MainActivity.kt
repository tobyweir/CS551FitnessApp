package com.example.cs551fitnessapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat

import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cs551fitnessapp.database.Exercise

import com.example.cs551fitnessapp.ui.navigation.AppNavHost
import com.example.cs551fitnessapp.ui.navigation.BottomBar
import com.example.cs551fitnessapp.ui.navigation.TopBar
import com.example.cs551fitnessapp.ui.screens.SearchWorkoutScreen
import com.example.cs551fitnessapp.ui.theme.CS551FitnessAppTheme
import com.example.cs551fitnessapp.ui.navigation.AppNavGraph
import com.example.cs551fitnessapp.ui.screens.WorkoutPlanScreen
import com.example.cs551fitnessapp.ui.viewmodels.WorkoutPlanViewModel


class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Permission result handled
    }


    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        
        //Request permission for notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            CS551FitnessAppTheme {

                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val canGoBack = navBackStackEntry != null && navController.previousBackStackEntry != null
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopBar(
                            navController = navController,
                            showBackIcon = canGoBack
                        )
                    },
                    bottomBar = {
                        BottomBar(navController)
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
    @SuppressLint("ViewModelConstructorInComposable")
    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        CS551FitnessAppTheme {
            WorkoutPlanScreen(
                planViewModel = WorkoutPlanViewModel(),
                onBackClick = { },
                onAddWorkout = { },
                onCancelClick = { },
                onDoneClick = { }
            )
        }
    }
}
