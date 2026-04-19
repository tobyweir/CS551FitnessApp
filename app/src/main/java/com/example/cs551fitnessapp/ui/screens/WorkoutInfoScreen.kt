package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.cs551fitnessapp.R
import com.example.cs551fitnessapp.database.Exercise
import com.example.cs551fitnessapp.database.WorkoutEntry

private val PrimaryBlue  = Color(0xFF2962FF)
private val LightBlueBg  = Color(0xFFE8EAF6)
private val GrayButton   = Color(0xFF757575)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutInfoScreen(
    exercise    : Exercise,
    onBackClick : () -> Unit,
    onCancelClick: () -> Unit,
    onAddClick  : (WorkoutEntry) -> Unit,
    modifier: Modifier
) {

    var sets    by remember { mutableStateOf("3")  }
    var reps    by remember { mutableStateOf("12") }
    var timeHr by remember { mutableStateOf("00") }
    var timeMin by remember { mutableStateOf("00") }
    var note    by remember { mutableStateOf("")   }

    val focusSets    = remember { FocusRequester() }
    val focusReps    = remember { FocusRequester() }
    val focusTimeHr = remember { FocusRequester() }
    val focusTimeMin = remember { FocusRequester() }
    val focusNote    = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current


    val context = LocalContext.current
    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .components { add(GifDecoder.Factory()) }
            .build()
    }

    Scaffold(modifier = modifier,
        topBar = {
            WorkoutInfoTopBar(onBackClick = onBackClick)
        },
        bottomBar = {
            BottomActionBar(
                onCancelClick = onCancelClick,
                onAddClick    = {
                    onAddClick(
                        WorkoutEntry(
                            exercise = exercise, //exercise info from API e.g. name, gif, etc
                            sets     = sets.toIntOrNull()    ?: 0,
                            reps     = reps.toIntOrNull()    ?: 0,
                            timeHr  = timeHr.toIntOrNull() ?: 0,
                            timeMin  = timeMin.toIntOrNull() ?: 0,
                            note     = note
                        )
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            // Exercise GIF image
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(Color.White)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(exercise.gifUrl)
                        .crossfade(true)
                        .build(),
                    imageLoader        = imageLoader,
                    contentDescription = exercise.name,
                    contentScale       = ContentScale.Fit,
                    modifier           = Modifier
                        .fillMaxHeight()
                        .padding(24.dp)
                )
            }

            Divider(color = Color(0xFFEEEEEE))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                // Exercise name
                Text(
                    text       = exercise.name,
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color      = PrimaryBlue,
                    textAlign  = TextAlign.Center,
                    modifier   = Modifier.fillMaxWidth()
                )

                // Body Parts
                BodyPartMusclesRow(muscles = exercise.bodyParts)

                // Set × Replies
                SetRepsRow(
                    sets    = sets,
                    reps    = reps,
                    onSetsChange = { sets = it.filter(Char::isDigit).take(2) },
                    onRepsChange = { reps = it.filter(Char::isDigit).take(2) },
                    leftField = focusSets,
                    rightField = focusReps,
                    nextField = focusTimeHr

                )

                // Duration
                DurationRow(
                    hours       = timeHr,
                    minutes       = timeMin,
                    onHrChange   = { timeHr = it.filter(Char::isDigit).take(2) },
                    onMinChange   = { timeMin = it.filter(Char::isDigit).take(2) },
                    leftField = focusTimeHr,
                    rightField = focusTimeMin,
                    nextField = focusNote
                )

                // Notes
                NoteField(
                    value    = note,
                    onChange = { note = it },
                    focusRequester = focusNote,
                    onImeAction    = { focusManager.clearFocus() }
                )

                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

// Top bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkoutInfoTopBar(onBackClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text       = "Workout Info",
                fontWeight = FontWeight.Bold,
                color      = PrimaryBlue,
                fontSize   = 20.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector        = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint               = PrimaryBlue
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

// Target muscles row
@Composable
private fun BodyPartMusclesRow(muscles: List<String>) {
    val muscleText = muscles.joinToString(", ")
        .replaceFirstChar { it.uppercase() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Dumbbell icon placeholder — swap for your own drawable if needed
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFEEEEEE))
        ) {
            Image(
                painter = painterResource(id = R.drawable.dumbbell_hand),
                contentDescription = "Add",
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text     = "Body Parts : $muscleText",
            fontSize = 15.sp,
            color    = Color(0xFF424242)
        )
    }
}

// Set × Replies row
@Composable
private fun SetRepsRow(
    sets         : String,
    reps         : String,
    onSetsChange : (String) -> Unit,
    onRepsChange : (String) -> Unit,
    leftField    : FocusRequester,
    rightField   : FocusRequester,
    nextField : FocusRequester
) {

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text     = "Set",
                fontSize = 15.sp,
                color    = Color(0xFF424242),
                modifier = Modifier.weight(1f)

            )
            Spacer(Modifier.width(16.dp))   // the "X" column gap
            Text(
                text     = "Replies",
                fontSize = 15.sp,
                color    = Color(0xFF424242),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            verticalAlignment  = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Sets field
            RoundedNumberField(
                value    = sets,
                onChange = onSetsChange,
                modifier = Modifier.weight(1f),
                focusRequester = leftField,
                imeAction      = ImeAction.Next,
                onImeAction    = { rightField.requestFocus() }
            )

            Text(
                text     = "x",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color    = Color(0xFF424242)
            )

            // Reps field
            RoundedNumberField(
                value    = reps,
                onChange = onRepsChange,
                modifier = Modifier.weight(1f),
                focusRequester = rightField,
                imeAction      = ImeAction.Next,
                onImeAction    = { nextField.requestFocus() }
            )
        }
    }
}

// Time row
@Composable
private fun DurationRow(
    hours     : String,
    minutes     : String,
    onHrChange : (String) -> Unit,
    onMinChange : (String) -> Unit,
    leftField    : FocusRequester,
    rightField   : FocusRequester,
    nextField : FocusRequester
) {

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text     = "Duration (hh:mm)",
            fontSize = 15.sp,
            color    = Color(0xFF424242)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RoundedNumberField(
                value    = hours,
                onChange = onHrChange,
                modifier = Modifier.weight(1f),
                focusRequester = leftField,
                imeAction      = ImeAction.Next,
                onImeAction    = { rightField.requestFocus() }
            )

            Text(
                text       = ":",
                fontSize   = 22.sp,
                fontWeight = FontWeight.Bold,
                color      = Color(0xFF424242)
            )

            RoundedNumberField(
                value    = minutes,
                onChange = onMinChange,
                modifier = Modifier.weight(1f),
                focusRequester = rightField,
                imeAction      = ImeAction.Next,
                onImeAction    = { nextField.requestFocus() }
            )
        }
    }
}

// Notes
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteField(
    value: String,
    onChange: (String) -> Unit,
    focusRequester : FocusRequester,
    onImeAction    : () -> Unit
) {
    TextField(
        value         = value,
        onValueChange = onChange,
        placeholder   = { Text("e.g. 40 lbs", color = PrimaryBlue.copy(alpha = 0.6f)) },
        shape         = RoundedCornerShape(14.dp),
        colors        = TextFieldDefaults.colors(
            focusedContainerColor   = LightBlueBg,
            unfocusedContainerColor = LightBlueBg,
            focusedIndicatorColor   = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor        = PrimaryBlue,
            unfocusedTextColor      = PrimaryBlue
        ),
        minLines = 2,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction    = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onImeAction() }
        ),
        modifier = Modifier.fillMaxWidth()
            .focusRequester(focusRequester)
    )
}

// rounded number field
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoundedNumberField(
    value    : String,
    onChange : (String) -> Unit,
    modifier : Modifier = Modifier,
    focusRequester : FocusRequester,
    imeAction      : ImeAction,
    onImeAction    : () -> Unit
) {
    TextField(
        value         = value,
        onValueChange = onChange,
        singleLine    = true,
        textStyle     = LocalTextStyle.current.copy(
            textAlign  = TextAlign.Center,
            fontSize   = 16.sp,
            color      = Color(0xFF424242)
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction    = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { onImeAction() }
        ),
        shape   = RoundedCornerShape(50),
        colors  = TextFieldDefaults.colors(
            focusedContainerColor   = LightBlueBg,
            unfocusedContainerColor = LightBlueBg,
            focusedIndicatorColor   = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier.height(52.dp)
            .focusRequester(focusRequester)
    )
}

// Bottom bar
@Composable
private fun BottomActionBar(
    onCancelClick : () -> Unit,
    onAddClick    : () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .navigationBarsPadding()
    ) {
        // Cancel
        Button(
            onClick = onCancelClick,
            shape   = RoundedCornerShape(50),
            colors  = ButtonDefaults.buttonColors(containerColor = GrayButton),
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
        ) {
            Text("Cancel", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }

        // Add
        Button(
            onClick = onAddClick,
            shape   = RoundedCornerShape(50),
            colors  = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
        ) {
            Text("Add", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}