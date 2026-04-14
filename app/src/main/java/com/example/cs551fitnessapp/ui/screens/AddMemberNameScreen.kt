package com.example.cs551fitnessapp.ui.screens

import android.net.Uri

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

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

import androidx.compose.ui.platform.LocalContext

import com.example.cs551fitnessapp.R



@Composable
fun AddMemberNameScreen(

    onBackClick: () -> Unit,

    onSaveClick: () -> Unit,

    modifier: Modifier = Modifier

) {

    var name by remember {

        mutableStateOf("Jessica J.")

    }



    var sessions by remember {

        mutableStateOf("20")

    }



    var selectedImageUri by remember {

        mutableStateOf<Uri?>(null)

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

                fontWeight = FontWeight.SemiBold

            )



            Spacer(Modifier.height(10.dp))



            Text(

                "What's your name?",

                fontSize = 28.sp,

                fontWeight = FontWeight.Bold

            )



            Spacer(Modifier.height(10.dp))



            Text(

                "Enter member details below",

                fontSize = 13.sp,

                color = Color.Gray

            )



            Spacer(Modifier.height(20.dp))



            Box(

                contentAlignment = Alignment.Center

            ) {



                Image(

                    painter = painterResource(

                        id = R.drawable.ic_launcher_background   // default image

                    ),

                    contentDescription = "profile",

                    contentScale = ContentScale.Crop,

                    modifier = Modifier

                        .size(120.dp)

                        .clip(CircleShape)

                        .clickable {

                            // later we add gallery picker

                        }

                )

            }



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

                textStyle = androidx.compose.ui.text.TextStyle(

                    fontSize = 26.sp,

                    fontWeight = FontWeight.Medium

                ),

                singleLine = true,

                modifier = Modifier.fillMaxWidth()

            )



            Spacer(Modifier.height(20.dp))



            Row(

                verticalAlignment = Alignment.CenterVertically

            ) {



                Text(

                    "Num Of Session :",

                    fontSize = 14.sp,

                    color = Color.Gray

                )



                Spacer(Modifier.width(12.dp))



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

            onClick = onSaveClick,

            colors = ButtonDefaults.buttonColors(

                containerColor = Color(0xFF2E7D32)

            ),

            shape = RoundedCornerShape(50),

            modifier = Modifier

                .fillMaxWidth()

                .height(55.dp)

        ) {

            Text(

                "Save",

                fontSize = 16.sp

            )

        }

    }

}