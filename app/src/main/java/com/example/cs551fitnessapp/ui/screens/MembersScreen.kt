package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager

import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController

import com.example.cs551fitnessapp.R

import java.util.Date



// Added image property
data class Member(
    val id : Int,
    val name : String,
    val joinDate : Date,
    val endDate : Date?,
    val status: String,
    val image : Int
)



val members = listOf(

    Member(
        0,
        "John Smith",
        Date(2026 , 3 , 17),
        null,
        "Active",
        R.drawable.profile1
    ),

    Member(
        1,
        "Mike Smith",
        Date(2026 , 3 , 17),
        null,
        "Active",
        R.drawable.profile2
    ),

    Member(
        2,
        "Major Smith",
        Date(2026 , 3 , 17),
        null,
        "Active",
        R.drawable.profile3
    )

)



@Composable
fun MembersScreen(
    navController: NavController
){

    Scaffold(

        floatingActionButton = { AddMemberButton() }

    ) { innerPadding ->

        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

            SearchBar()

            SortingButtons()

            MembersList(
                members = members,
                navController = navController
            )

        }

    }

}





@Composable
fun SearchBar(){

    val focusManager = LocalFocusManager.current

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),

        shape = RoundedCornerShape(0.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2962FF)
        )

    ) {

        Column(

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Center,

            modifier = Modifier.fillMaxSize()

        ) {

            OutlinedTextField(

                value = "",

                onValueChange = {},

                leadingIcon = {

                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )

                },

                modifier = Modifier.fillMaxWidth(0.85f),

                shape = RoundedCornerShape(100.dp),

                singleLine = true,

                placeholder = {

                    Text(
                        "Search...",
                        style = TextStyle(
                            fontSize = 16.sp
                        )
                    )

                }

            )

        }

    }

}



@Composable
fun SortingButtons(){

    Row(

        horizontalArrangement = Arrangement.SpaceEvenly,

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)

    ) {

        ElevatedButton(onClick = {}) {

            Text("Active")

        }

        ElevatedButton(onClick = {}) {

            Text("Nearly Finished")

        }

        ElevatedButton(onClick = {}) {

            Text("Inactive")

        }

    }

}





@Composable
fun AddMemberButton(){

    FloatingActionButton(

        onClick = {}

    ) {

        Icon(
            Icons.Default.Add,
            contentDescription = null
        )

    }

}





@Composable
fun MembersList(

    members : List<Member>,

    navController: NavController

){

    LazyColumn(

        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier.fillMaxSize()

    ) {

        items(

            items = members,

            key = { it.id }

        ) { item ->


            MemberCard(

                member = item,

                navController = navController,

                modifier = Modifier
                    .fillMaxWidth()
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

            )

        }

    }

}





@Composable
fun MemberCard(

    member : Member,

    navController: NavController,

    modifier: Modifier

){

    Row(

        modifier = modifier
            .clickable {

                navController.navigate(

                    "member_info/${member.name}/${member.image}"

                )

            }
            .padding(10.dp)

    ) {


        Image(

            painter = painterResource(member.image),

            contentDescription = null,

            contentScale = ContentScale.Crop,

            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)

        )



        Spacer(modifier = Modifier.width(16.dp))



        Column(

            verticalArrangement = Arrangement.SpaceAround

        ) {

            Text(

                text = member.name,

                color = Color.Blue,

                fontWeight = FontWeight.Bold,

                fontSize = 18.sp

            )


            Text(

                text = "Join : ${member.joinDate.date}/${member.joinDate.month}/${member.joinDate.year}",

                fontSize = 14.sp

            )



            Text(

                text = "Status : ${member.status}",

                fontSize = 14.sp

            )

        }

    }

}