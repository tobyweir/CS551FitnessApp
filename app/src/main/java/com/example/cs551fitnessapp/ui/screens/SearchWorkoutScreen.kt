package com.example.cs551fitnessapp.ui.screens

//-- Search Workout Screen ----------------------------------------------------------

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.cs551fitnessapp.R
import com.example.cs551fitnessapp.database.Exercise
import com.example.cs551fitnessapp.database.WorkoutEntry
import com.example.cs551fitnessapp.ui.viewmodels.SearchWorkoutViewModel
import com.example.cs551fitnessapp.ui.viewmodels.states.ExerciseUiState
import com.example.cs551fitnessapp.ui.viewmodels.WorkoutPlanViewModel
import kotlinx.coroutines.flow.distinctUntilChanged


private val PrimaryBlue = Color(0xFF2962FF)
private val GrayButton  = Color(0xFF757575)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchWorkoutScreen(
    planViewModel  : WorkoutPlanViewModel,
    onBackClick    : () -> Unit,
    onSaveClick    : () -> Unit,
    onAddExercise  : (Exercise) -> Unit,
    searchViewModel: SearchWorkoutViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val searchQuery  by searchViewModel.searchQuery.collectAsState()
    val uiState      by searchViewModel.uiState.collectAsState()
    val addedEntries by planViewModel.addedEntries.collectAsState() //workout list

    LaunchedEffect(Unit) {
        searchViewModel.reloadIfIdle()
    }

    Scaffold(
        bottomBar = {
            BottomBar(
                onCancelClick = onBackClick,
                onSaveClick   = onSaveClick
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // -- Search field ----------------------------------------------------------------------
            TopSearchHeader(
                query         = searchQuery,
                onQueryChange = searchViewModel::onSearchQueryChange,
                onBackClick   = onBackClick
            )

            // -- Search results --------------------------------------------------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)           // expands to fill available space, 1f = takes remaining space
            ) {
                when (val state = uiState) {

                    is ExerciseUiState.Loading -> {
                        CircularProgressIndicator(
                            color    = PrimaryBlue,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    is ExerciseUiState.Error -> {
                        Text(
                            text     = "Error: ${state.message}",
                            color    = Color.Red,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }

                    is ExerciseUiState.Success -> {
                        SearchResultList(
                            exercises     = state.exercises,
                            planViewModel = planViewModel,
                            onAddExercise = onAddExercise,
                            onLoadMore    = searchViewModel::loadNextPage
                        )
                    }

                    else -> {
                        Text(
                            text     = "Search for an exercise above",
                            color    = Color.Gray,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            // -- Workout List ----------------------------------------------------------------------
            if (addedEntries.isNotEmpty()) {
                WorkoutListSection(
                    entries  = addedEntries,
                    onRemove = { id -> planViewModel.removeEntry(id) }
                )
            }
        }
    }
}

// ----------------------------------------------------------------------
// Search results — LazyColumn
// ----------------------------------------------------------------------

@Composable
private fun SearchResultList(
    exercises     : List<Exercise>,
    planViewModel : WorkoutPlanViewModel,
    onAddExercise : (Exercise) -> Unit,
    onLoadMore    : () -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= exercises.size - 5
        }
    }
    //    snapshotFlow across rotation
    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo  = listState.layoutInfo
            val totalItems  = layoutInfo.totalItemsCount
            if (totalItems == 0) return@snapshotFlow false

            val lastVisible = layoutInfo.visibleItemsInfo
                .lastOrNull()?.index ?: 0

            lastVisible >= totalItems - 5
        }
            .distinctUntilChanged()
            .collect { nearEnd ->
                if (nearEnd) onLoadMore()
            }
    }

    LazyColumn(
        state            = listState,
        contentPadding   = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier         = Modifier.fillMaxSize()
    ) {
        itemsIndexed(exercises, key = { _, ex -> ex.id }) { _, exercise ->
            ExerciseSearchRow(
                exercise   = exercise,
                isAdded    = planViewModel.isAdded(exercise.id),
                onAddClick = { onAddExercise(exercise) }
            )
        }

        // Pagination spinner
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color       = PrimaryBlue,
                    modifier    = Modifier.size(22.dp),
                    strokeWidth = 2.dp
                )
            }
        }
    }
}

// ----------------------------------------------------------------------
// Workout List
// ----------------------------------------------------------------------

@Composable
private fun WorkoutListSection(
    entries  : List<WorkoutEntry>,
    onRemove : (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .heightIn(max = 260.dp)  // caps section height
    ) {
        // Section title
        Text(
            text       = "Workout List",
            fontSize   = 17.sp,
            fontWeight = FontWeight.Bold,
            color      = PrimaryBlue,
            modifier   = Modifier.padding(
                start  = 20.dp,
                top    = 12.dp,
                bottom = 6.dp
            )
        )

        // Scrollable list of workout
//        LazyColumn(
//            contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            modifier            = Modifier.fillMaxWidth()
//        ) {
//            itemsIndexed(
//                entries,
//                key = { idx, entry -> "${entry.exercise.id}_$idx" }
//            ) { _, entry ->
//                WorkoutListRow(
//                    entry    = entry,
//                    onRemove = { onRemove(entry.exercise.id) }
//                )
//            }
//            item { Spacer(Modifier.height(4.dp)) }
//        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())  // scrollable within 260dp cap
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            entries.forEach { entry ->
                WorkoutListRow(
                    entry    = entry,
                    onRemove = { onRemove(entry.exercise.id) }
                )
            }
            Spacer(Modifier.height(4.dp))
        }
    }
}

// ----------------------------------------------------------------------
// Top bar + Search field
// ----------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopSearchHeader(
    query         : String,
    onQueryChange : (String) -> Unit,
    onBackClick   : () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBlue)
            .padding(bottom = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "Search Workout",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        TextField(
            value         = query,
            onValueChange = onQueryChange,
            placeholder   = { Text("Search...", color = Color(0xFF9E9E9E)) },
            leadingIcon   = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint               = Color(0xFF9E9E9E)
                )
            },
            singleLine    = true,
            shape         = RoundedCornerShape(50),
            colors        = TextFieldDefaults.colors(
                focusedContainerColor   = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor   = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

// ----------------------------------------------------------------------
// Exercise search result content
// ----------------------------------------------------------------------

@Composable
private fun ExerciseSearchRow(
    exercise   : Exercise,
    isAdded    : Boolean,
    onAddClick : () -> Unit
) {
    val context     = LocalContext.current
    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .components { add(GifDecoder.Factory()) }
            .build()
    }

    Card(
        shape     = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
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
                    .size(68.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text       = exercise.name,
                fontSize   = 15.sp,
                fontWeight = FontWeight.Medium,
                maxLines   = 2,
                overflow   = TextOverflow.Ellipsis,
                color      = Color(0xFF212121),
                modifier   = Modifier.weight(1f)
            )

            IconButton(onClick = onAddClick) {
                Icon(
                    imageVector        = Icons.Default.Add,
                    contentDescription = "Add ${exercise.name}",
                    tint               = if (isAdded) Color(0xFFE0FE10) else PrimaryBlue,
                    modifier           = Modifier.size(28.dp)
                )
            }
        }
    }
}

// ----------------------------------------------------------------------
// Added workout row
// ----------------------------------------------------------------------

@Composable
private fun WorkoutListRow(
    entry    : WorkoutEntry,
    onRemove : () -> Unit
) {
    Card(
        shape     = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors    = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
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
                    .background(PrimaryBlue.copy(alpha = 0.12f))
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

// ----------------------------------------------------------------------
// Bottom bar
// ----------------------------------------------------------------------

@Composable
private fun BottomBar(
    onCancelClick : () -> Unit,
    onSaveClick   : () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .navigationBarsPadding()
    ) {
        Button(
            onClick  = onCancelClick,
            shape    = RoundedCornerShape(50),
            colors   = ButtonDefaults.buttonColors(containerColor = GrayButton),
            modifier = Modifier.weight(1f).height(52.dp)
        ) {
            Text("Cancel", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }

        Button(
            onClick  = onSaveClick,
            shape    = RoundedCornerShape(50),
            colors   = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
            modifier = Modifier.weight(1f).height(52.dp)
        ) {
            Text("Save", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}