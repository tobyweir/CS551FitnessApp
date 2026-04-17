package com.example.cs551fitnessapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.cs551fitnessapp.database.member.MemberEntity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cs551fitnessapp.R
import com.example.cs551fitnessapp.ui.ViewModelFactory
import com.example.cs551fitnessapp.ui.navigation.AddMemberFlow
import com.example.cs551fitnessapp.ui.navigation.MemberPage
import com.example.cs551fitnessapp.ui.theme.CS551FitnessAppTheme
import com.example.cs551fitnessapp.ui.viewmodels.MembersViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MembersScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewmodel: MembersViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val uiState = viewmodel.uiState.collectAsState()
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            AddMemberButton(
                onClick = { navController.navigate(AddMemberFlow) }
            )
        },
        floatingActionButtonPosition = FabPosition.EndOverlay
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SearchBar(
                currentSearch = viewmodel.searchEntry,
                updateSearchQuery = { viewmodel.searchEntry = it },
                runSearch = { viewmodel.doSearch() }
            )
            SortingButtons(
                isActive = uiState.value.includeActive,
                isInactive = uiState.value.includeInactive,
                isNearlyFinished = uiState.value.includeNearlyFinished,
                { viewmodel.pressActiveButton() },
                { viewmodel.pressNearlyFinishedButton() },
                { viewmodel.pressInactiveButton() }
            )
            MembersList(
                members = uiState.value.sortedMembers,
                navController = navController
            )
        }
    }
}


@Composable
fun SearchBar(currentSearch : String = "" , updateSearchQuery : (String) -> Unit , runSearch : () -> Unit) {
    val focusManager = LocalFocusManager.current
    Card (modifier = Modifier.fillMaxWidth().fillMaxHeight(0.15f) , shape = RectangleShape, colors = CardDefaults.cardColors(containerColor = Color.Blue)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center , modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            OutlinedTextField(
                leadingIcon = {Icon(imageVector = Icons.Default.Search , contentDescription = "Search icon" )},
                value = currentSearch,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    ,
                onValueChange = updateSearchQuery
                ,
                shape = RoundedCornerShape(100.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White

                ),
                placeholder = {
                    Text(
                        text = "Search...",
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.LightGray
                        )
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    runSearch()
                    focusManager.clearFocus()
                })
            )
        }
    }
}

@Composable
fun SortingButtons( isActive : Boolean , isInactive : Boolean , isNearlyFinished : Boolean , selectActive : () -> Unit , selectNearlyFinished : () -> Unit , selectInactive : () -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly , modifier = Modifier.fillMaxWidth().padding(top = 10.dp , bottom = 10.dp)) {
        ElevatedButton(onClick = {} , enabled = isActive,  ) {
            Box (modifier = Modifier.clickable(onClick = selectActive)) {
                Text(text = "Active")
            }
        }
        ElevatedButton(onClick = {} , enabled = isNearlyFinished) {
            Box (modifier = Modifier.clickable(onClick = selectNearlyFinished)) {
                Text(text = "Nearly Finished")
            }
        }
        ElevatedButton(onClick = {} , enabled = isInactive) {
            Box (modifier = Modifier.clickable(onClick = selectInactive)) {
                Text(text = " Inactive")
            }
        }
    }
}

@Composable
fun AddMemberButton(onClick : () -> Unit = {}) {
    Button(onClick = onClick) { Icon(imageVector = Icons.Default.Add , contentDescription = "Add a new member") }
}
//drawBehind modifier taken from https://stackoverflow.com/questions/68592618/how-to-add-border-on-bottom-only-in-jetpack-compose
@Composable
fun MembersList(navController: NavHostController, members: List<MemberEntity>, modifier: Modifier = Modifier) {
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally , modifier = modifier.fillMaxSize()) {
        items(items = members, key = { it.memberId }) { item ->
                MemberCard(
                    member = item, modifier = Modifier.fillMaxSize()
                        .drawBehind {
                            val strokeWidth = 1 * density
                            val y = size.height - strokeWidth / 2
                            drawLine(
                                Color.LightGray,
                                Offset(1f, y),
                                Offset(size.width, y),
                                strokeWidth
                            )
                        }
                        .clickable(onClick = { navController.navigate(MemberPage(item.memberId.toInt())) }))
        }
    }
}
@Composable
fun MemberCard(member: MemberEntity, modifier: Modifier = Modifier.fillMaxSize()){
    Row(modifier = modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center ,) {
        MemberCardImage(modifier = Modifier
            .fillMaxWidth(0.3f)
            .padding(10.dp))
        MemberCardInfo(
            name = member.name,
            joinDate = member.joinDate,
            endDate = member.endDate,
            status = member.status,
            modifier = Modifier.fillMaxWidth(0.7f)
        )
    }
}

@Composable
fun MemberCardImage(modifier: Modifier = Modifier.fillMaxSize()) {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = "Image of member",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
    )

}

@Composable
fun MemberCardInfo(
    name: String,
    joinDate: Long,
    endDate: Long?,
    status: String,
    modifier: Modifier
) {
    Column(verticalArrangement = Arrangement.SpaceAround , modifier = modifier.fillMaxHeight().padding (top = 10.dp , start = 10.dp , end = 10.dp , bottom = 10.dp)) {
        Text(text = name , Modifier.padding(bottom = 3.dp) , color = Color.Blue , fontWeight = FontWeight.Bold, style = TextStyle(fontSize = 18.sp))

        Text(
            text = "Start Date : ${formatMemberDate(joinDate)}",
            modifier = Modifier.padding(bottom = 3.dp),
            style = TextStyle(fontSize = 14.sp)
        )

        Text(
            text = "End Date : ${formatMemberDate(endDate)}",
            modifier = Modifier.padding(bottom = 3.dp),
            style = TextStyle(fontSize = 14.sp)
        )

        StatusBadge(status)
    }
}


@Composable
fun StatusBadge(status: String) {
    val backgroundColor = when (status) {
        "Active" -> Color(0xFFDFF5E3)
        "Inactive" -> Color(0xFFFDE2E1)
        "Nearly Finished" -> Color(0xFFFFF4CC)
        else -> Color.LightGray
    }
}

private fun formatMemberDate(timestamp: Long?): String {
    if (timestamp == null) return "No date set"
    val formatter = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CS551FitnessAppTheme {
        //MembersScreen()
    }
}