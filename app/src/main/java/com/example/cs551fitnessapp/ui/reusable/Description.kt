package com.example.cs551fitnessapp.ui.reusable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.cs551fitnessapp.R

@Composable
fun Description (headText: Int, descriptionText: Int, modifier: Modifier = Modifier) {
    val descText = stringResource(descriptionText)
    Column(modifier = modifier) {
        Text(
            text = stringResource(headText),
            style = MaterialTheme.typography.displayMedium
        )
        if (descText.isNotBlank()) {
            Text(text = descText)
        }
    }
}