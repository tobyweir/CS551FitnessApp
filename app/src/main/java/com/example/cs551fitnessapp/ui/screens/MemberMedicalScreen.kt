package com.example.cs551fitnessapp.ui.screens

// -- Medical concern sceen on Add new member flow ------------------------------------------------

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PersonalInjury
import androidx.compose.material.icons.filled.Vaccines

private val PrimaryBlue = Color(0xFF2962FF)
private val LightGrayBg = Color(0xFFF5F5F5)


data class MedicalOption(
    val id          : Int,
    val title       : String,
    val description : String,
    val icon        : ImageVector
)

private val medicalOptions = listOf(
    MedicalOption(0, "None",        "No any concern.", Icons.Default.EmojiEmotions),
    MedicalOption(1, "Surgery",     "I had surgery on some parts of my body.", Icons.Default.Vaccines),
    MedicalOption(2, "Muscle pain", "I have muscle pain in some areas.", Icons.Default.PersonalInjury),
    MedicalOption(3, "Other",       "Please describe your medical concern.", Icons.Default.MoreHoriz)
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalConcernScreen(

    onBackClick   : () -> Unit = {},
    onNextClick   : (selectedId: Int, note: String) -> Unit = { _, _ -> },
    modifier: Modifier
) {
    // -- State ------------------------------------------------
    var selectedId by remember { mutableStateOf(0) }
    var note       by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier,
        topBar = {
            MedicalTopBar(onBackClick = onBackClick)
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
                    onClick  = { onNextClick(selectedId, note) },
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
                text       = "Medical concern",
                fontSize   = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = Color(0xFF111111)
            )

            Text(
                text       = "Choose the option that best matches your concern, whether it’s surgery,  " +
                        "muscle pain or another issue, so we can guide you to the most appropriate support.",
                fontSize   = 13.sp,
                color      = Color(0xFF757575),
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(4.dp))

            // -- Option cards ------------------------------------------------
            medicalOptions.forEach { option ->
                MedicalOptionCard(
                    option     = option,
                    isSelected = selectedId == option.id,
                    onClick    = { selectedId = option.id }
                )
            }

            // -- Medical concern note text field --------------------------------
            TextField(
                value         = note,
                onValueChange = { note = it },
                placeholder   = {
                    Text(
                        "Enter your concern here...",
                        color    = Color(0xFFBDBDBD),
                        fontSize = 14.sp
                    )
                },
                shape   = RoundedCornerShape(16.dp),
                colors  = TextFieldDefaults.colors(
                    focusedContainerColor   = LightGrayBg,
                    unfocusedContainerColor = LightGrayBg,
                    focusedIndicatorColor   = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}

// -----------------------------------------------------------------------------------
// Top bar
// -----------------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicalTopBar(
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
// Medical option card
// -----------------------------------------------------------------------------

@Composable
private fun MedicalOptionCard(
    option     : MedicalOption,
    isSelected : Boolean,
    onClick    : () -> Unit
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