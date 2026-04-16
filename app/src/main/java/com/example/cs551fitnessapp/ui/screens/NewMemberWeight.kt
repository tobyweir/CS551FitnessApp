package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cs551fitnessapp.R
import com.example.cs551fitnessapp.ui.reusable.ChoiceButton
import com.example.cs551fitnessapp.ui.reusable.Description
import com.example.cs551fitnessapp.ui.reusable.NextScreenButton
import com.example.cs551fitnessapp.ui.theme.CS551FitnessAppTheme

@Composable
fun WeightScreen (modifier: Modifier = Modifier , onBackClick : () -> Unit = {} , onNextClick : () -> Unit = {}) {
    Column(
        modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Description(R.string.Weight, R.string.Sample_Text)

        ChoiceButton(R.string.Pounds, R.string.Kilograms)

        NextScreenButton(onNextClick)
    }
}

@Preview(showBackground = true)
@Composable
fun WeightPreview () {
    CS551FitnessAppTheme() {
        WeightScreen()
    }
}