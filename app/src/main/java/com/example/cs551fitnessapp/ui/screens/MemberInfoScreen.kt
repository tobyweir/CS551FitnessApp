package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.cs551fitnessapp.R
import com.example.cs551fitnessapp.ui.viewmodels.states.MemberUiState



@Composable
fun MemberInfoScreen(
    id : Int,
    member: MemberUiState = MemberUiState(
        name = "Test",
        image = R.drawable.profile1
    ),
    modifier: Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {

            MemberHeader(member)

            StatsSection()

            UpcomingSection()

            PreviousSection()

            Spacer(modifier = Modifier.height(90.dp))
        }



        ExtendedFloatingActionButton(

            onClick = { },

            containerColor = Color(0xFFD7FF00),

            icon = {

                Icon(Icons.Default.Add, null)

            },

            text = {

                Text(
                    "Session",
                    fontWeight = FontWeight.Bold
                )

            },

            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}






@Composable
fun MemberHeader(
    member: MemberUiState
) {

    Card(

        shape = RoundedCornerShape(30.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDDE3F5)
        ),

        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 20.dp,   // increased spacing from top bar
                bottom = 8.dp
            )
            .fillMaxWidth()

    ) {

        Column(

            modifier = Modifier.padding(20.dp)

        ) {

            Row {

                Image(

                    painter = painterResource(member.image),

                    contentDescription = null,

                    contentScale = ContentScale.Crop,

                    modifier = Modifier
                        .size(95.dp)
                        .clip(CircleShape)

                )


                Spacer(modifier = Modifier.width(16.dp))


                Column {

                    AssistChip(

                        onClick = { },

                        label = {

                            Text("Lose weight")

                        }

                    )


                    Spacer(modifier = Modifier.height(10.dp))


                    Card(

                        shape = RoundedCornerShape(14.dp),

                        colors = CardDefaults.cardColors(

                            containerColor = Color(0xFF2E5BFF)

                        )

                    ) {

                        Text(

                            text = "Info : Lorem ipsum dolor sit amet, consectetur adipiscing elit.",

                            color = Color.White,

                            modifier = Modifier.padding(12.dp),

                            fontSize = 13.sp

                        )
                    }

                }

            }



            Spacer(modifier = Modifier.height(18.dp))


            Card(

                shape = RoundedCornerShape(50),

                colors = CardDefaults.cardColors(

                    containerColor = Color.White

                ),

                modifier = Modifier.fillMaxWidth()

            ) {

                Text(

                    text = member.name,

                    modifier = Modifier.padding(12.dp),

                    color = Color(0xFF2E5BFF),

                    fontWeight = FontWeight.Bold

                )
            }



            Spacer(modifier = Modifier.height(12.dp))


            Row(

                verticalAlignment = Alignment.CenterVertically

            ) {

                Icon(

                    Icons.Default.DateRange,

                    contentDescription = null,

                    tint = Color(0xFF2E5BFF)

                )


                Spacer(modifier = Modifier.width(6.dp))


                Text(

                    "Sat 5 Jan 2026 / 9:00AM",

                    color = Color.Gray

                )

            }



            Spacer(modifier = Modifier.height(12.dp))


            Button(

                onClick = { },

                colors = ButtonDefaults.buttonColors(

                    containerColor = Color(0xFF2E5BFF)

                )

            ) {

                Icon(Icons.Default.DateRange, null)

                Spacer(modifier = Modifier.width(6.dp))

                Text("Schedule")

            }

        }

    }

}





@Composable
fun StatsSection() {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 10.dp),

        horizontalArrangement = Arrangement.SpaceBetween

    ) {

        Column {

            Text(
                "Training Hours",
                color = Color.Gray
            )


            Text(
                "17.50",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

        }



        Column {

            Text(
                "Session",
                color = Color.Gray
            )


            Text(
                "18 / 20",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

        }

    }
}







@Composable
fun UpcomingSection() {

    SectionTitle("Upcoming")

    SessionRow(
        title = "Cardio Session",
        duration = "60 mins",
        date = "5 Jan 2026"
    )

}





@Composable
fun PreviousSection() {

    SectionTitle("Previous Session")

    SessionRow(
        title = "Strength Session",
        duration = "30 mins",
        date = "30 Dec 2025"
    )


    SessionRow(
        title = "Mixed",
        duration = "90 mins",
        date = "25 Dec 2025"
    )

}





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







@Composable
fun SessionRow(
    title: String,
    duration: String,
    date: String
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),

        verticalAlignment = Alignment.CenterVertically

    ) {


        Box(

            modifier = Modifier
                .size(46.dp)
                .background(
                    Color(0xFF2E5BFF),
                    CircleShape
                ),

            contentAlignment = Alignment.Center

        ) {

            Icon(
                Icons.Default.DateRange,
                contentDescription = null,
                tint = Color.White
            )

        }



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



        Text(
            ">",
            fontSize = 18.sp,
            color = Color.Gray
        )

    }
}