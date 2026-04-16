package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel


import com.example.cs551fitnessapp.R

import com.example.cs551fitnessapp.ui.reusable.BirthdayDatePicker

import com.example.cs551fitnessapp.ui.reusable.Description

import com.example.cs551fitnessapp.ui.reusable.NextScreenButton

import com.example.cs551fitnessapp.ui.theme.CS551FitnessAppTheme

import com.example.cs551fitnessapp.ui.viewmodels.MemberBirthdayViewModel



@Composable
fun BirthdayScreen(

    modifier: Modifier = Modifier,

    onNextClick: () -> Unit = {},

    onBackClick: () -> Unit = {}

) {


    val viewModel: MemberBirthdayViewModel = viewModel()



    Column(

        modifier = modifier

            .fillMaxSize()

            .padding(20.dp)

    ) {



        Column(

            modifier = Modifier

                .weight(1f)

                .verticalScroll(

                    rememberScrollState()

                ),

            verticalArrangement = Arrangement.spacedBy(20.dp)

        ) {



            Description(

                R.string.Birthday,

                R.string.Sample_Text

            )



            OutlinedTextField(

                value = viewModel.birthday.value,

                onValueChange = {

                    viewModel.updateBirthday(it)

                },

                label = {

                    Text("Enter Date of Birth (DD/MM/YYYY)")

                },

                placeholder = {

                    Text("e.g. 21/05/1998")

                },

                singleLine = true,

                modifier = Modifier.fillMaxWidth()

            )



            BirthdayDatePicker(

                onDateSelected = { selectedDate ->

                    viewModel.updateBirthday(selectedDate)

                }

            )

        }



        NextScreenButton(

            onNextClick,

            modifier = Modifier

                .fillMaxWidth()

                .padding(top = 20.dp)

        )

    }

}




@Preview(showBackground = true)

@Composable

fun BirthdayPreview() {

    CS551FitnessAppTheme {

        BirthdayScreen()

    }

}