package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import com.example.cs551fitnessapp.ui.viewmodels.states.MemberUiState



@Composable
fun MemberInfoScreen(
    member: MemberUiState
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {

            ProfileCard(member)

            StatsSection()

            UpcomingSection()

            PreviousSection()

            Spacer(modifier = Modifier.height(100.dp))
        }



        ExtendedFloatingActionButton(

            onClick = { },

            icon = {
                Icon(Icons.Default.Add, null)
            },

            text = {
                Text("Session")
            },

            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}



/*
PROFILE CARD
*/

@Composable
fun ProfileCard(member: MemberUiState) {

    Card(

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDDE3F5)
        ),

        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.padding(16.dp)
        ) {


            Box(

                modifier = Modifier
                    .size(90.dp)

                    .background(
                        Color.LightGray,
                        CircleShape
                    )
            )


            Spacer(modifier = Modifier.width(16.dp))



            Column {

                Text(

                    text = member.name,

                    fontSize = 20.sp,

                    fontWeight = FontWeight.Bold
                )


                Spacer(modifier = Modifier.height(6.dp))


                Text(

                    text = "Member information",

                    color = Color.Gray
                )
            }
        }
    }
}



/*
STATS (placeholder for now)
*/

@Composable
fun StatsSection() {

    Row(

        modifier = Modifier
            .fillMaxWidth()

            .padding(horizontal = 20.dp, vertical = 10.dp),

        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            Text(
                "Training Hours",
                color = Color.Gray
            )

            Text(
                "0",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }



        Column {

            Text(
                "Sessions",
                color = Color.Gray
            )

            Text(
                "0 / 0",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}



/*
UPCOMING
*/

@Composable
fun UpcomingSection() {

    SectionTitle("Upcoming")

    SessionRow("Session", "60 mins", "date")
}



/*
PREVIOUS
*/

@Composable
fun PreviousSection() {

    SectionTitle("Previous Session")

    SessionRow("Session", "45 mins", "date")

    SessionRow("Session", "30 mins", "date")
}



/*
SECTION TITLE
*/

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



/*
SESSION ROW
*/

@Composable
fun SessionRow(

    title: String,
    duration: String,
    date: String

) {

    Row(

        modifier = Modifier
            .fillMaxWidth()

            .padding(
                horizontal = 20.dp,
                vertical = 12.dp
            )
    ) {


        Box(

            modifier = Modifier
                .size(40.dp)

                .background(
                    Color(0xFF2E5BFF),
                    CircleShape
                )
        )


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



        Text(">")
    }
}