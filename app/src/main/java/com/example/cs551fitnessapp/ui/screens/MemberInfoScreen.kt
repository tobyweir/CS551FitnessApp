package com.example.cs551fitnessapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cs551fitnessapp.ui.ViewModelFactory
import com.example.cs551fitnessapp.ui.navigation.AddWorkoutFlow
import com.example.cs551fitnessapp.ui.viewmodels.MemberViewModel
import com.example.cs551fitnessapp.ui.viewmodels.states.MemberSessionItem
import com.example.cs551fitnessapp.ui.viewmodels.states.MemberUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MemberInfoScreen(
    id: Int,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MemberViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(id) {
        viewModel.loadMember(id.toLong())
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.name.isBlank() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Member not found")
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    MemberHeader(member = uiState)
                    UpcomingSection(uiState.upcomingSessions)
                    PreviousSection(uiState.previousSessions)
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = { navController.navigate(AddWorkoutFlow(id)) },
            containerColor = Color(0xFFD7FF00),
            icon = {
                Icon(Icons.Default.Add, contentDescription = null)
            },
            text = {
                Text(
                    "Session",
                    fontWeight = FontWeight.Bold
                )
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(16.dp)
        )
    }
}

@Composable
fun MemberHeader(
    member: MemberUiState
) {
    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 20.dp,
                bottom = 8.dp
            )
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .size(95.dp)
                        .background(color = MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = member.name.firstOrNull()?.uppercase() ?: "",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    AssistChip(
                        onClick = { },
                        label = {
                            Text("Goal: ${member.goal.ifBlank { "Not set" }}")
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2E5BFF)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = member.notes
                                .replace(Regex("""\s*\|\s*Sessions:\s*[^|]+"""), "")
                                .ifBlank { "No notes yet." },
                            color = Color.White,
                            modifier = Modifier.padding(12.dp),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                shape = RoundedCornerShape(50),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = member.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    color = Color(0xFF2E5BFF),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color(0xFF2E5BFF)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Date Joined: ${formatJoinDate(member.joinDate)}",
                    color = Color.Gray
                )
            }
        }
    }
}

private fun formatJoinDate(joinDate: Long?): String {
    if (joinDate == null) return "Unknown"
    val formatter = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
    return formatter.format(Date(joinDate))
}

@Composable
fun UpcomingSection(
    sessions: List<MemberSessionItem>
) {
    SectionTitle("Upcoming")

    if (sessions.isEmpty()) {
        EmptySectionText("No upcoming sessions")
    } else {
        sessions.forEach { session ->
            SessionRow(
                title = session.sessionName,
                duration = session.durationText,
                date = "${session.startText}  •  Ends ${session.endText}"
            )
        }
    }
}

@Composable
fun PreviousSection(
    sessions: List<MemberSessionItem>
) {
    SectionTitle("Previous Session")

    if (sessions.isEmpty()) {
        EmptySectionText("No previous sessions")
    } else {
        sessions.forEach { session ->
            SessionRow(
                title = session.sessionName,
                duration = session.durationText,
                date = "${session.startText}  •  Ended ${session.endText}"
            )
        }
    }
}

@Composable
fun EmptySectionText(
    text: String
) {
    Text(
        text = text,
        color = Color.Gray,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    )
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier.padding(
            start = 20.dp,
            top = 20.dp
        ),
        color = Color(0xFF2E5BFF)
    )
}

@Composable
fun SessionRow(
    title: String,
    duration: String,
    date: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(
                    Color(0xFF2E5BFF),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.DateRange,
                contentDescription = null,
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                title,
                fontWeight = FontWeight.Bold
            )

            Text(
                "$duration • $date",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}