package com.example.cs551fitnessapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cs551fitnessapp.ui.ViewModelFactory
import com.example.cs551fitnessapp.ui.navigation.SessionDetail
import com.example.cs551fitnessapp.ui.viewmodels.MemberSession
import com.example.cs551fitnessapp.ui.viewmodels.TodayViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayScreen(
    navController: NavController,
    modifier: Modifier,
    viewmodel: TodayViewModel = viewModel(factory = ViewModelFactory.Factory),
    isWide : Boolean = false
) {
    val uiState = viewmodel.uiState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        DateSelector(
            selectedDate = uiState.value.selectedDay,
            onDateSelected = { viewmodel.updateSelectedDay(it) },
            dates = listOf(
                uiState.value.day1,
                uiState.value.day2,
                uiState.value.day3,
                uiState.value.day4,
                uiState.value.day5,
                uiState.value.day6,
                uiState.value.day7
            )
        )

        Text(
            text = "Schedule for Today",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 4.dp)
        )

        if (uiState.value.sessions.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("No sessions yet", color = Color.Gray)
            }
        } else {
            if (!isWide) {
                LazyColumn {
                    items(
                        items = uiState.value.sessions,
                        key = { it.sessionId }
                    ) { session ->
                        SessionCard(
                            session = session,
                            navController = navController
                        )
                    }
                }
            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(count = 3)) {
                    items(
                        items = uiState.value.sessions,
                        key = { it.sessionId }
                    ) { session ->
                        SessionCard(
                            session = session,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateSelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    dates: List<LocalDate>
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(vertical = 10.dp, horizontal = 0.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(dates) {
            DateItem(
                day = it.dayOfMonth.toString(),
                date = it.dayOfWeek.name.take(3),
                isSelected = it == selectedDate,
                onClick = { onDateSelected(it) }
            )
        }
    }
}

@Composable
fun DateItem(
    day: String,
    date: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(68.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.background
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                day,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                date,
                fontSize = 12.sp,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SessionCard(
    session: MemberSession,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                navController.navigate(SessionDetail(session.sessionId))
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = session.memberName.firstOrNull()?.uppercase() ?: "?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = session.memberName,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = session.sessionName,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text("Start: ${session.startTime}" ,
                    color = MaterialTheme.colorScheme.onBackground)
                Text("End: ${session.endTime}" ,
                    color = MaterialTheme.colorScheme.onBackground)
                Text("Duration: ${session.duration}" ,
                    color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}