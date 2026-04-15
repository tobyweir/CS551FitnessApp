package com.example.cs551fitnessapp.ui.screens

import android.R.attr.clickable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cs551fitnessapp.R
import com.example.cs551fitnessapp.ui.ViewModelFactory
import com.example.cs551fitnessapp.ui.navigation.MemberPage
import com.example.cs551fitnessapp.ui.viewmodels.MemberSession
import com.example.cs551fitnessapp.ui.viewmodels.TodayViewModel
import java.time.LocalDate

data class Workout(
    val id : Int,
    val date : LocalDate,
    val start: String,
    val duration : String,
    val memberId : Int
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayScreen(
    navController: NavController , modifier: Modifier, viewmodel: TodayViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val uiState = viewmodel.uiState.collectAsState()
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        DateSelector(
            selectedDate = uiState.value.selectedDay,
            onDateSelected = {viewmodel.updateSelectedDay(it) },
            dates = listOf(uiState.value.day1,
                uiState.value.day2,
                uiState.value.day3,
                uiState.value.day4,
                uiState.value.day5,
                uiState.value.day6,
                uiState.value.day7,
            )
        )
        if (uiState.value.members.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier         = Modifier.fillMaxSize()
            ) {
                Text("No members yet", color = Color.Gray)
            }
        } else {
        LazyColumn {
//            items(uiState.value.filteredWorkouts) {
//                val currWorkout = it
//                SessionCard(
//                    workout = it,
//                    member = uiState.value.members.first{ it.id == currWorkout.memberId },
//                    navController = navController
//                )
//            }
            // ── Member rows ────────────────────────────────────────────
            items(
                items = uiState.value.members,
                key = { it.id }
            ) { member ->
                //MemberRow(member = member)
                SessionCard(
                    //workout = it.se,
                    member = uiState.value.members.first { it.id == member.id }, //session id
                    navController = navController
                )
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
    dates : List<LocalDate>
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE3E8FF))
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
            .height(80.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) Color(0xFF2962FF)
                else Color.White
            )
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                day,
                color = if (isSelected) Color.White else Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                date,
                fontSize = 12.sp,
                color = if (isSelected) Color.White else Color.Black
            )
        }
    }
}

@Composable
fun SessionCard(
    //workout: Workout,
    member: MemberSession,
    navController: NavController,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
        .clickable { navController.navigate(MemberPage(member.memberId)) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDCE3F3)
        )

    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.profile1),
                contentDescription = "Member Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(member.name, //this is session name
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2962FF))
                Text("Start : ${member.dtSessionStart}")
                Text("End : ${member.dtSessionEnd}")
                Row {
                    Button(
                        onClick = { /*navController.navigate() */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5C6BC0))
                    ) { Text("Info") }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.DateRange, contentDescription = null)
                    }
                }
            }
        }
    }
}


