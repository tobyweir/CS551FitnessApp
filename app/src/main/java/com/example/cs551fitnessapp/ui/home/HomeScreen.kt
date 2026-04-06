package com.example.cs551fitnessapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.cs551fitnessapp.R

private val Icons.Filled.FitnessCenter: ImageVector

@Composable
fun HomeScreen(
    onWorkoutClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onProgressClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {

    Scaffold(
        bottomBar = {
            BottomNavBar(onSettingsClick)
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            item {
                TitleSection()
            }

            item {
                QuickActionsRow(
                    onWorkoutClick,
                    onScheduleClick
                )
            }

            item {
                DashboardGrid(
                    onWorkoutClick,
                    onScheduleClick,
                    onProgressClick,
                    onProfileClick
                )
            }
        }
    }
}

@Composable
fun TitleSection() {

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Text(
            text = "Fitness X",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Track your fitness journey",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun QuickActionsRow(
    onWorkoutClick: () -> Unit,
    onScheduleClick: () -> Unit
) {

    val actions = listOf(

        Triple(
            "Strength Training",
            "30 mins",
            R.drawable.workout_bg
        ),

        Triple(
            "Running Session",
            "6:40/km",
            R.drawable.running_bg
        )
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(actions) { action ->

            Card(
                modifier = Modifier
                    .width(260.dp)
                    .height(160.dp)
                    .clickable {

                        if (action.first.contains("Strength"))
                            onWorkoutClick()
                        else
                            onScheduleClick()
                    },
                shape = RoundedCornerShape(20.dp)
            ) {

                Box {

                    Image(
                        painter = painterResource(action.third),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {

                        Text(
                            text = action.first,
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = action.second,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardGrid(
    onWorkoutClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onProgressClick: () -> Unit,
    onProfileClick: () -> Unit
) {

    val features = listOf(

        Triple(
            "Workout",
            Icons.Default.FitnessCenter,
            onWorkoutClick
        ),

        Triple(
            "Schedule",
            Icons.Default.DateRange,
            onScheduleClick
        ),

        Triple(
            "Progress",
            Icons.Default.ShowChart,
            onProgressClick
        ),

        Triple(
            "Profile",
            Icons.Default.Person,
            onProfileClick
        )
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.height(340.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(features) { feature ->

            Card(
                modifier = Modifier
                    .height(130.dp)
                    .clickable { feature.third() },

                shape = RoundedCornerShape(18.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),

                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Icon(
                        imageVector = feature.second,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )

                    Text(
                        text = feature.first,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(
    onSettingsClick: () -> Unit
) {

    NavigationBar {

        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.DateRange, null) },
            label = { Text("Today") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile") }
        )

        NavigationBarItem(
            selected = false,
            onClick = onSettingsClick,
            icon = { Icon(Icons.Default.Settings, null) },
            label = { Text("Settings") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen()
}