package com.example.cs551fitnessapp.ui.reusable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
    fun NextScreenButton (onClick: () -> Unit, modifier: Modifier = Modifier){
        Button(
            onClick = onClick,
            modifier
                .fillMaxWidth()
        ) {
            Text(text = "Next")
        }
    }
