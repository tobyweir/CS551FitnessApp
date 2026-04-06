package com.example.cs551fitnessapp.ui.today
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
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.tooling.preview.Preview

import com.example.cs551fitnessapp.R



data class Session(
    val name: String,
    val start: String,
    val duration: String,
    val progress: String,
    val image: Int
)

@Composable
fun TodayScreen() {

    var selectedDate by remember { mutableStateOf("24") }

    val sessions = listOf(

        Session(
            "Alexander Bennett",
            "10:00 AM",
            "1 hr",
            "2/10",
            R.drawable.profile1
        ),

        Session(
            "Jessica J.",
            "02:00 PM",
            "1 hr",
            "8/10",
            R.drawable.profile2
        ),

        Session(
            "Olivia Turner",
            "05:30 PM",
            "1 hr",
            "3/15",
            R.drawable.profile3
        ),

        Session(
            "Sophia M.",
            "07:00 PM",
            "2 hr",
            "4/10",
            R.drawable.profile4
        )
    )

    Scaffold(

        bottomBar = { BottomNavBar() }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            Header()

            DateSelector(selectedDate) {

                selectedDate = it
            }

            LazyColumn {

                items(sessions) {

                    SessionCard(it)
                }
            }
        }
    }
}

@Composable
fun Header() {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(Icons.Default.ArrowBack, null)

        Text(

            "Today",

            style = MaterialTheme.typography.titleLarge,

            fontWeight = FontWeight.Bold
        )

        Icon(Icons.Default.Notifications, null)
    }
}

@Composable
fun DateSelector(

    selectedDate: String,

    onDateSelected: (String) -> Unit
) {

    val dates = listOf(
        "21","22","23","24","25","26","27"
    )

    LazyRow(

        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE3E8FF))
            .padding(12.dp),

        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        items(dates) {

            DateItem(

                day = it,

                isSelected = it == selectedDate,

                onClick = {

                    onDateSelected(it)
                }
            )
        }
    }
}

@Composable
fun DateItem(

    day: String,

    isSelected: Boolean,

    onClick: () -> Unit
) {

    Box(

        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(

                if (isSelected)

                    Color(0xFF2962FF)

                else

                    Color.White
            )
            .padding(

                horizontal = 18.dp,

                vertical = 12.dp
            )
            .clickable { onClick() }

    ) {

        Text(

            day,

            color =

                if (isSelected)

                    Color.White

                else

                    Color.Black
        )
    }
}

@Composable
fun SessionCard(session: Session) {

    Card(

        modifier = Modifier
            .padding(16.dp),

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(

            containerColor = Color(0xFFE3E8FF)
        )
    ) {

        Row(

            modifier = Modifier.padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(

                painter = painterResource(session.image),

                contentDescription = null,

                contentScale = ContentScale.Crop,

                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(

                modifier = Modifier.weight(1f)
            ) {

                Text(

                    session.name,

                    fontWeight = FontWeight.Bold,

                    color = Color(0xFF2962FF)
                )

                Text("Start : ${session.start}")

                Text("Duration : ${session.duration}")

                Row {

                    Button(

                        onClick = { }

                    ) {

                        Text("Info")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(

                        onClick = { }

                    ) {

                        Icon(Icons.Default.DateRange, null)
                    }
                }
            }

            AssistChip(

                onClick = { },

                label = {

                    Text(

                        "session ${session.progress}"
                    )
                }
            )
        }
    }
}

@Composable
fun BottomNavBar() {

    NavigationBar {

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.DateRange, null) },
            label = { Text("Today") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Member") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Settings, null) },
            label = { Text("Settings") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewToday() {

    TodayScreen()
}