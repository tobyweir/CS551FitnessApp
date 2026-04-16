package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.layout.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle

import androidx.compose.material3.Icon
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlinx.coroutines.delay

@Composable
fun MemberSuccessScreen(
    onFinished: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1000)
        onFinished()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Successful",
            color = Color(0xFF2962FF)
        )
        Spacer(Modifier.height(40.dp))
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF2962FF),
            modifier = Modifier.size(120.dp)
        )
        Spacer(Modifier.height(20.dp))
        Text(
            "Congratulation",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2962FF)
        )

        Text(
            "New member added",
            color = Color(0xFF2962FF)
        )
    }
}