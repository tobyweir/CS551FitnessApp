package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PersonalInjury
import androidx.compose.material.icons.filled.Vaccines

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



private val PrimaryBlue = androidx.compose.ui.graphics.Color(0xFF2962FF)
private val LightGrayBg = androidx.compose.ui.graphics.Color(0xFFF5F5F5)



data class MedicalOption(

    val id: Int,
    val title: String,
    val description: String,
    val icon: ImageVector

)



private val medicalOptions = listOf(

    MedicalOption(
        0,
        "None",
        "No medical concern.",
        Icons.Default.EmojiEmotions
    ),

    MedicalOption(
        1,
        "Surgery",
        "Previous surgery on body.",
        Icons.Default.Vaccines
    ),

    MedicalOption(
        2,
        "Muscle pain",
        "Pain in muscles or joints.",
        Icons.Default.PersonalInjury
    ),

    MedicalOption(
        3,
        "Other",
        "Describe your concern.",
        Icons.Default.MoreHoriz
    )

)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalConcernScreen(

    modifier: Modifier = Modifier,

    onBackClick: () -> Unit = {},

    onNextClick: (selectedId: Int, note: String) -> Unit

) {

    var selectedId by remember { mutableStateOf(0) }

    var note by remember { mutableStateOf("") }



    Scaffold(

        modifier = modifier,

        topBar = {

            MedicalTopBar(

                onBackClick = onBackClick

            )

        },

        bottomBar = {

            Box(

                modifier = Modifier

                    .fillMaxWidth()

                    .background(MaterialTheme.colorScheme.surface)

                    .padding(horizontal = 24.dp, vertical = 16.dp)

                    .navigationBarsPadding()

            ) {

                Button(

                    onClick = {

                        onNextClick(selectedId, note)

                    },

                    shape = RoundedCornerShape(50),

                    modifier = Modifier

                        .fillMaxWidth()

                        .height(54.dp)

                ) {

                    Text(

                        "Next",

                        fontSize = 17.sp,

                        fontWeight = FontWeight.SemiBold

                    )

                }

            }

        },

        containerColor = MaterialTheme.colorScheme.background

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

                text = "Medical concern",

                fontSize = 30.sp,

                fontWeight = FontWeight.ExtraBold,

                color = MaterialTheme.colorScheme.onBackground

            )



            Text(

                text = "Choose the option that best matches your concern.",

                fontSize = 13.sp,

                lineHeight = 20.sp,

                color = MaterialTheme.colorScheme.onSurfaceVariant

            )



            Spacer(Modifier.height(4.dp))



            medicalOptions.forEach { option ->

                MedicalOptionCard(

                    option = option,

                    isSelected = selectedId == option.id,

                    onClick = { selectedId = option.id }

                )

            }



            TextField(

                value = note,

                onValueChange = { note = it },

                placeholder = {

                    Text("Additional notes")

                },

                shape = RoundedCornerShape(16.dp),

                minLines = 4,

                modifier = Modifier.fillMaxWidth()

            )



            Spacer(Modifier.height(8.dp))

        }

    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicalTopBar(

    onBackClick: () -> Unit

) {

    CenterAlignedTopAppBar(

        title = {

            Text(

                "Add Member",

                fontWeight = FontWeight.Bold,

                color = PrimaryBlue

            )

        },

        navigationIcon = {

            IconButton(onClick = onBackClick) {

                Icon(

                    Icons.AutoMirrored.Filled.ArrowBack,

                    contentDescription = "Back",

                    tint = PrimaryBlue

                )

            }

        },

        colors = TopAppBarDefaults.topAppBarColors(

            containerColor = MaterialTheme.colorScheme.surface

        )

    )

}



@Composable
private fun MedicalOptionCard(

    option: MedicalOption,

    isSelected: Boolean,

    onClick: () -> Unit

) {

    Card(

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(

            containerColor = LightGrayBg

        ),

        modifier = Modifier

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

                    imageVector = option.icon,

                    contentDescription = option.title,

                    tint = androidx.compose.ui.graphics.Color.White

                )

            }



            Spacer(Modifier.width(14.dp))



            Column(

                modifier = Modifier.weight(1f)

            ) {

                Text(

                    option.title,

                    fontWeight = FontWeight.SemiBold

                )



                Text(

                    option.description,

                    fontSize = 12.sp,

                    color = MaterialTheme.colorScheme.onSurfaceVariant

                )

            }



            if (isSelected) {

                Box(

                    modifier = Modifier

                        .size(26.dp)

                        .clip(CircleShape)

                        .background(PrimaryBlue),

                    contentAlignment = Alignment.Center

                ) {

                    Icon(

                        Icons.Default.Check,

                        contentDescription = "selected",

                        tint = androidx.compose.ui.graphics.Color.White

                    )

                }

            } else {

                Box(

                    modifier = Modifier

                        .size(26.dp)

                        .clip(CircleShape)

                        .border(

                            2.dp,

                            MaterialTheme.colorScheme.outline,

                            CircleShape

                        )

                )

            }

        }

    }

}