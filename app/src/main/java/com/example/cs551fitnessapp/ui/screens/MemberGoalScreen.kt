package com.example.cs551fitnessapp.ui.screens

// -- Member goals screen on Add new member flow ------------------------------------------------

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.PersonalInjury
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.Vaccines

private val PrimaryBlue = Color(0xFF2962FF)
private val LightGrayBg = Color(0xFFF5F5F5)


data class GoalOption(
    val id          : Int,
    val title       : String,
    val description : String,
    val icon        : ImageVector
)

private val goalOptions = listOf(
    GoalOption(0, "Lose Weight",        "My goal is to lose weight", Icons.Default.MonitorWeight),
    GoalOption(1, "Build Muscle",     "My goal is to build muscle in some areas.", Icons.Default.FitnessCenter),
    GoalOption(2, "Get Fit", "My goal is to get fit.", Icons.Default.SportsGymnastics),
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberGoalScreen(

    onBackClick   : () -> Unit = {},
    onNextClick   : (selectedGoalId: Int) -> Unit = { _ -> }
) {
    // -- State ------------------------------------------------
    var selectedGoalId by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            GoalTopBar(onBackClick = onBackClick)
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick  = { onNextClick(selectedGoalId) },
                    shape    = RoundedCornerShape(50),
                    colors   = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text(
                        text       = "Next",
                        fontSize   = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color.White
                    )
                }
            }
        },
        containerColor = Color.White
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text       = "Your goal",
                fontSize   = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = Color(0xFF111111)
            )

            Text(
                text       = "Select the option that best fits your needs such as building strength, " +
                        "losing weight  or general fitness, so your personal trainer can create a plan tailored to you.",
                fontSize   = 13.sp,
                color      = Color(0xFF757575),
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(4.dp))

            // -- Option cards ------------------------------------------------
            goalOptions.forEach { option ->
                GoalOptionCard(
                    option = option,
                    isSelected = selectedGoalId == option.id,
                    onClick = { selectedGoalId = option.id }
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

// -----------------------------------------------------------------------------------
// Top bar
// -----------------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoalTopBar(
    //memberInitial : String,
    onBackClick   : () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text       = "Add Member",
                fontWeight = FontWeight.Bold,
                color      = PrimaryBlue,
                fontSize   = 18.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint               = PrimaryBlue
                )
            }
        },
        actions = { /* Set title in centre of screen */
            Spacer(modifier = Modifier.width(48.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

// -----------------------------------------------------------------------------
// Goal option card
// -----------------------------------------------------------------------------

@Composable
private fun GoalOptionCard(
    option: GoalOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors    = CardDefaults.cardColors(containerColor = LightGrayBg),
        modifier  = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue)
            ) {
                Icon(
                    imageVector        = option.icon,
                    contentDescription = option.title,
                    tint               = Color.White,
                    modifier           = Modifier.size(26.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = option.title,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color(0xFF212121)
                )
                Text(
                    text     = option.description,
                    fontSize = 12.sp,
                    color    = Color(0xFF9E9E9E)
                )
            }

            Spacer(Modifier.width(12.dp))

            if (isSelected) {
                // Filled blue circle
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue)
                ) {
                    Icon(
                        imageVector        = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint               = Color.White,
                        modifier           = Modifier.size(16.dp)
                    )
                }
            } else {
                // Empty circle outline
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFFBDBDBD), CircleShape)
                ) {
                    Icon(
                        imageVector        = Icons.Default.Check,
                        contentDescription = null,
                        tint               = Color.Transparent,
                        modifier           = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}