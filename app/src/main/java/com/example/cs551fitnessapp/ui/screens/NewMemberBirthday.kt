package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cs551fitnessapp.R
import com.example.cs551fitnessapp.ui.reusable.BirthdayDatePicker
import com.example.cs551fitnessapp.ui.reusable.Description
import com.example.cs551fitnessapp.ui.reusable.NextScreenButton
import com.example.cs551fitnessapp.ui.theme.CS551FitnessAppTheme

@Composable
fun BirthdayScreen(
    birthday: String,
    onBirthdayChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Description(
                R.string.Birthday,
                R.string.Sample_Text
            )

            OutlinedTextField(
                value = birthday,
                onValueChange = { onBirthdayChange(it) },
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
                    onBirthdayChange(selectedDate)
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
        BirthdayScreen(
            birthday = "",
            onBirthdayChange = {},
            onBackClick = {},
            onNextClick = {}
        )
    }
}