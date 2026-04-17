package com.example.cs551fitnessapp.ui.reusable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
    fun NextScreenButton (onClick: () -> Unit, modifier: Modifier = Modifier, enabled : Boolean = true){
        Button(
            onClick = onClick,
            modifier
                .fillMaxWidth(),
            enabled = enabled,
        ) {
            Text(text = "Next" , color = Color.White)
        }
    }
