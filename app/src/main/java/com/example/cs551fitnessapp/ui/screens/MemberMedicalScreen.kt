package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cs551fitnessapp.ui.theme.CS551FitnessAppTheme

private val PrimaryBlue = Color(0xFF2962FF)
private val LightGrayBg = Color(0xFFF5F5F5)

data class MedicalOption(
    val id: Int,
    val title: String,
    val description: String,
    val icon: ImageVector
)

private val medicalOptions = listOf(
    MedicalOption(0, "None", "No known medical concern.", Icons.Default.Check),
    MedicalOption(1, "Heart Condition", "Client has a heart-related condition.", Icons.Default.Favorite),
    MedicalOption(2, "Injury", "Client has a current or past injury.", Icons.Default.Healing),
    MedicalOption(3, "Mobility Concern", "Client has a mobility-related concern.", Icons.Default.AccessibilityNew)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalConcernScreen(
    selectedConcernId: Int,
    note: String,
    onConcernSelected: (Int) -> Unit,
    onNoteChange: (String) -> Unit,
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Scaffold(
        modifier = modifier,
        topBar = {
            MedicalTopBar(onBackClick = onBackClick)
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = onNextClick,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text(
                        text = "Next",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
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
                text = "Select any medical concern that may affect training, and add a short note if needed.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(4.dp))

            medicalOptions.forEach { option ->
                MedicalOptionCard(
                    option = option,
                    isSelected = selectedConcernId == option.id,
                    onClick = { onConcernSelected(option.id) }
                )
            }

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = note,
                onValueChange = onNoteChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Additional note")
                },
                placeholder = {
                    Text("Add any relevant details")
                },
                minLines = 3,
                maxLines = 5
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
                text = "Add Member",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = {
            Spacer(modifier = Modifier.width(48.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
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
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        androidx.compose.foundation.layout.Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue)
            ) {
                Icon(
                    imageVector = option.icon,
                    contentDescription = option.title,
                    tint = Color.White
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = option.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = option.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(Modifier.width(12.dp))

            if (isSelected) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(28.dp)
                        .width(28.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .height(28.dp)
                        .width(28.dp)
                        .clip(CircleShape)
                        .background(Color.Transparent)
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MedicalConcernScreenPreview() {
//    CS551FitnessAppTheme {
//        MedicalConcernScreen(
//            selectedConcernId = 0,
//            note = "",
//            onConcernSelected = {},
//            onNoteChange = {},
//            onBackClick = {},
//            onNextClick = {}
//        )
//    }
//}