package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*

import androidx.compose.runtime.*

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



@Composable
fun AddMemberNameScreen(

    onBackClick: () -> Unit,

    onSaveClick: (String, Int) -> Unit,

    modifier: Modifier = Modifier

) {

    var name by remember {

        mutableStateOf("")

    }



    var sessions by remember {

        mutableStateOf("20")

    }



    Column(

        modifier = modifier

            .fillMaxSize()

            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.SpaceBetween

    ) {



        Column(

            horizontalAlignment = Alignment.CenterHorizontally

        ) {



            Text(

                "Add Member",

                color = Color(0xFF2962FF),

                fontWeight = FontWeight.Medium

            )



            Spacer(Modifier.height(10.dp))



            Text(

                "What's your name?",

                fontSize = 28.sp,

                fontWeight = FontWeight.Bold

            )



            Spacer(Modifier.height(20.dp))



            Image(

                painter = painterResource(

                    R.drawable.ic_launcher_background

                ),

                contentDescription = "profile",

                contentScale = ContentScale.Crop,

                modifier = Modifier

                    .size(120.dp)

                    .clip(CircleShape)

                    .clickable { }

            )



            Spacer(Modifier.height(8.dp))



            Text(

                "Edit",

                fontSize = 12.sp,

                color = Color.Gray

            )



            Spacer(Modifier.height(20.dp))



            OutlinedTextField(

                value = name,

                onValueChange = {

                    name = it

                },

                placeholder = {

                    Text("Full name")

                },

                modifier = Modifier.fillMaxWidth()

            )



            Spacer(Modifier.height(20.dp))



            Row(

                verticalAlignment = Alignment.CenterVertically

            ) {



                Text(

                    "Num Of Session :",

                    color = Color.Gray

                )



                Spacer(Modifier.width(10.dp))



                OutlinedTextField(

                    value = sessions,

                    onValueChange = {

                        sessions = it

                    },

                    singleLine = true,

                    shape = RoundedCornerShape(50),

                    modifier = Modifier.width(100.dp)

                )

            }

        }



        Button(

            onClick = {

                onSaveClick(

                    name,

                    sessions.toIntOrNull() ?: 0

                )

            },

            colors = ButtonDefaults.buttonColors(

                containerColor = Color(0xFF2E7D32)

            ),

            shape = RoundedCornerShape(50),

            modifier = Modifier

                .fillMaxWidth()

                .height(55.dp)

        ) {

            Text("Save")

        }

    }

}