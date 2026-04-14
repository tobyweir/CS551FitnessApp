package com.example.cs551fitnessapp.ui.screens

import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
fun BirthdayScreen (modifier: Modifier = Modifier , onNextClick : () -> Unit = {} , onBackClick : () -> Unit = {}) {
    Column(
        modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween

    ) {
        Description(R.string.Birthday, R.string.Sample_Text)

        BirthdayDatePicker()

        NextScreenButton(onNextClick)



    }
}



@Preview(showBackground = true)
@Composable
fun BirthdayPreview () {
    CS551FitnessAppTheme() {
        BirthdayScreen()
    }
}

