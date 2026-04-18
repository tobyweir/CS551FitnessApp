package com.example.cs551fitnessapp.ui.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cs551fitnessapp.database.WorkoutEntry
import com.example.cs551fitnessapp.ui.viewmodels.WorkoutPlanViewModel
import com.example.cs551fitnessapp.ui.components.TimePickerDialog
import androidx.compose.ui.res.painterResource
import com.example.cs551fitnessapp.R
import com.example.cs551fitnessapp.database.WorkoutPlanData
import com.example.cs551fitnessapp.ui.components.ErrorDialog
import com.example.cs551fitnessapp.ui.viewmodels.SavePlanResult
import com.example.cs551fitnessapp.ui.components.SuccessDialog
import java.util.*

private val PrimaryBlue = Color(0xFF2962FF)
private val LightBlueBg = Color(0xFFE8EAF6)
private val GrayButton  = Color(0xFF757575)
private val LightGray   = Color(0xFFBDBDBD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanScreen(
    //userId : Int?,
    planViewModel: WorkoutPlanViewModel,
    onBackClick: () -> Unit,
    onAddWorkout: () -> Unit,
    onCancelClick: () -> Unit,
    onDoneClick: (WorkoutPlanData) -> Unit,
    onNavigateToMemberInfo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val addedEntries by planViewModel.addedEntries.collectAsState() //Workout list
    val sessionName  by planViewModel.sessionName.collectAsState()
    val selectedDate by planViewModel.selectedDate.collectAsState()
    val startHour    by planViewModel.startHour.collectAsState()
    val startMin     by planViewModel.startMin.collectAsState()
    val endHour      by planViewModel.endHour.collectAsState()
    val endMin       by planViewModel.endMin.collectAsState()
    val saveResult   by planViewModel.saveResult.collectAsState()

    //Toast.makeText(LocalContext.current, userId.toString(), Toast.LENGTH_SHORT).show()

    // Timepicker
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker   by remember { mutableStateOf(false) }

 
    val isDoneEnabled = sessionName.isNotBlank()  &&
            selectedDate.isNotBlank() &&
            addedEntries.isNotEmpty()

    // Date picker
    val context    = LocalContext.current
    val calendar   = Calendar.getInstance()
    val monthNames = listOf(
        "Jan","Feb","Mar","Apr","May","Jun",
        "Jul","Aug","Sep","Oct","Nov","Dec"
    )

    val datePicker = DatePickerDialog(
        context,
        { _, year, month, day ->
            planViewModel.onSelectedDateChange(
                "%02d %s %d".format(day, monthNames[month], year)
            )
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    if (showStartPicker) {
        TimePickerDialog(
            initialHour   = startHour,
            initialMinute = startMin,
            onConfirm     = { h, m ->
                planViewModel.onStartHourChange(h)
                planViewModel.onStartMinChange(m)
                showStartPicker = false
            },
            onDismiss = { showStartPicker = false }
        )
    }

    if (showEndPicker) {
        TimePickerDialog(
            initialHour   = endHour,
            initialMinute = endMin,
            onConfirm     = { h, m ->
                planViewModel.onEndHourChange(h)
                planViewModel.onEndMinChange(m)
                showEndPicker = false
            },
            onDismiss = { showEndPicker = false }
        )
    }

    // Success Dialog
    if (saveResult is SavePlanResult.Success) {
        SuccessDialog(
            msg = "Workout plan has been saved successfully.",
            onDismiss = {
                planViewModel.clearAllValue()
                onNavigateToMemberInfo()
            }
        )
    }

    // Error Dialog
    if (saveResult is SavePlanResult.Error) {
        ErrorDialog( errormsg = (saveResult as SavePlanResult.Error).message, onDismiss = {planViewModel.resetSaveResult()}, onBtnOk = {planViewModel.resetSaveResult()})
    }

    Scaffold(
        topBar    = { WorkoutPlanTopBar(onBackClick = onBackClick) },
        bottomBar = {
            WorkoutPlanBottomBar(
                isDoneEnabled = isDoneEnabled && (saveResult !is SavePlanResult.Loading),
                onCancelClick = onCancelClick,
                onDoneClick   = {
                    onDoneClick(
                        WorkoutPlanData(
                            sessionName = sessionName,
                            date = selectedDate,
                            startHour = startHour,
                            startMin = startMin,
                            endHour = endHour,
                            endMin = endMin,
                            entries = addedEntries
                        )
                    )
                }
            )
        },
        containerColor = Color.White,
        modifier = modifier
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (saveResult is SavePlanResult.Loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = PrimaryBlue)
            }

            Text(
                text       = "Workout Details",
                fontSize   = 18.sp,
                fontWeight = FontWeight.Bold,
                color      = PrimaryBlue
            )

            // Session Name
            PlanField(label = "Session Name") {
                PlanTextField(
                    value       = sessionName,
                    onChange    = planViewModel::onSessionNameChange,
                    placeholder = "e.g. Upper Arms Session",
                    isError     = sessionName.isBlank()
                )
            }

            // Date
            PlanField(label = "Date") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (selectedDate.isBlank()) Color(0xFFFFEBEE) else LightBlueBg //validate before done
                        )
                        .clickable { datePicker.show() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text     = selectedDate.ifBlank { "DD MMM YYYY" },
                            fontSize = 15.sp,
                            color    = if (selectedDate.isBlank()) Color(0xFF9E9E9E)
                            else Color(0xFF212121),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Pick date",
                            tint               = Color(0xFF616161),
                            modifier           = Modifier.size(20.dp)
                        )
                    }
                }
                if (selectedDate.isBlank()) {
                    Text(
                        text     = "Please select a date",
                        fontSize = 11.sp,
                        color    = Color(0xFFE53935),
                        modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                    )
                }
            }

            // Start & End timepicker buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier              = Modifier.fillMaxWidth()
            ) {
                // Start
                Column(
                    modifier            = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Start", fontSize = 14.sp, color = Color(0xFF424242))
                    TimePickerButton(
                        hour    = startHour,
                        minute  = startMin,
                        onClick = { showStartPicker = true }
                    )
                }

                // End
                Column(
                    modifier            = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("End", fontSize = 14.sp, color = Color(0xFF424242))
                    TimePickerButton(
                        hour    = endHour,
                        minute  = endMin,
                        onClick = { showEndPicker = true }
                    )
                }
            }

            // Workout List header
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text       = "Workout List",
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = PrimaryBlue
                )
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(
                    onClick = onAddWorkout,
                    modifier = Modifier
                        .size(24.dp) // The actual diameter of the circle
                        .border(1.dp, PrimaryBlue, CircleShape)
                        .clip(CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add workout",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(24.dp) // Icon is significantly smaller than the circle
                    )
                }
            }

            // Workout List body
            if (addedEntries.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(12.dp))
                        .background(Color.White)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text      = "Click + to add workout item",
                            fontSize  = 14.sp,
                            color     = Color(0xFF9E9E9E),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text      = "At least 1 exercise is required",
                            fontSize  = 11.sp,
                            color     = Color(0xFFE53935),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    addedEntries.forEach { entry ->
                        PlanWorkoutRow(
                            entry    = entry,
                            onRemove = { planViewModel.removeEntry(entry.exercise.id) }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

// Time picker button
@Composable
private fun TimePickerButton(
    hour    : Int,
    minute  : Int,
    onClick : () -> Unit
) {
    val timeText = "%02d : %02d".format(hour, minute)
    val isUnset  = hour == 0 && minute == 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(LightBlueBg)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 13.dp)
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier              = Modifier.fillMaxWidth()
        ) {
            Text(
                text       = timeText,
                fontSize   = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color      = if (isUnset) Color(0xFF9E9E9E) else Color(0xFF2962FF)
            )
            Icon(
                imageVector        = Icons.Default.AccessTime,
                contentDescription = "Pick time",
                tint               = if (isUnset) Color(0xFF9E9E9E) else Color(0xFF2962FF),
                modifier           = Modifier.size(18.dp)
            )
        }
    }
}

// Content layout
@Composable
private fun PlanField(
    label   : String,
    content : @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, fontSize = 14.sp, color = Color(0xFF424242))
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlanTextField(
    value       : String,
    onChange    : (String) -> Unit,
    placeholder : String,
    isError     : Boolean = false
) {
    TextField(
        value         = value,
        onValueChange = onChange,
        placeholder   = { Text(placeholder, color = Color(0xFF9E9E9E), fontSize = 15.sp) },
        singleLine    = true,
        isError       = isError,
        shape         = RoundedCornerShape(50),
        colors        = TextFieldDefaults.colors(
            focusedContainerColor   = LightBlueBg,
            unfocusedContainerColor = if (isError) Color(0xFFFFEBEE) else LightBlueBg,
            errorContainerColor     = Color(0xFFFFEBEE),
            focusedIndicatorColor   = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor     = Color.Transparent,
            focusedTextColor        = Color(0xFF212121),
            unfocusedTextColor      = Color(0xFF212121)
        ),
        modifier = Modifier.fillMaxWidth()
    )
    if (isError) {
        Text(
            text     = "Session name is required",
            fontSize = 11.sp,
            color    = Color(0xFFE53935),
            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
        )
    }
}

// Workout List row
@Composable
private fun PlanWorkoutRow(
    entry    : WorkoutEntry,
    onRemove : () -> Unit
) {
    Card(
        shape     = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors    = CardDefaults.cardColors(containerColor = LightBlueBg),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue.copy(alpha = 0.15f))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dumbbell_blue),
                    contentDescription = "Add",
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = entry.exercise.name
                        .split(" ")
                        .joinToString(" ") { it.replaceFirstChar(Char::uppercase) },
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color(0xFF212121),
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(3.dp))
                val notePart = entry.note.takeIf { it.isNotBlank() }
                val setRep   = "${entry.sets} × ${entry.reps}"
                Text(
                    text     = listOfNotNull(notePart, setRep).joinToString("  |  "),
                    fontSize = 13.sp,
                    color    = Color(0xFF757575)
                )
            }
            IconButton(onClick = onRemove) {
                Text("✕", fontSize = 14.sp, color = Color(0xFFBDBDBD))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkoutPlanTopBar(onBackClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text("Workout Plan", fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 20.sp)
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryBlue)
            }
        },
        actions = { /* Set title in centre of screen */
            Spacer(modifier = Modifier.width(48.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
private fun WorkoutPlanBottomBar(
    isDoneEnabled : Boolean,
    onCancelClick : () -> Unit,
    onDoneClick   : () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .navigationBarsPadding()
    ) {
        Button(
            onClick  = onCancelClick,
            shape    = RoundedCornerShape(50),
            colors   = ButtonDefaults.buttonColors(containerColor = GrayButton),
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
        ) {
            Text("Cancel", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
        Button(
            onClick  = onDoneClick,
            enabled  = isDoneEnabled,
            shape    = RoundedCornerShape(50),
            colors   = ButtonDefaults.buttonColors(
                containerColor         = PrimaryBlue,
                disabledContainerColor = LightGray
            ),
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
        ) {
            Text("Done", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}
