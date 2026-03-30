package com.example.cs551fitnessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cs551fitnessapp.navigation.AppNavHost
import com.example.cs551fitnessapp.navigation.BottomBar
import com.example.cs551fitnessapp.navigation.TopBar
import com.example.cs551fitnessapp.ui.theme.CS551FitnessAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CS551FitnessAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val canGoBack = navBackStackEntry != null &&
                        navController.previousBackStackEntry != null
                Scaffold(modifier = Modifier.fillMaxSize() , topBar = {TopBar(navController = navController , showBackIcon = canGoBack)} , bottomBar = { BottomBar(navController = navController) }) { innerPadding ->
                    AppNavHost(navController , modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CS551FitnessAppTheme {
        Greeting("Android")
    }
}