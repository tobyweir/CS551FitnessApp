package com.example.cs551fitnessapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

private val PrimaryBlue = Color(0xFF2962FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialHour   : Int,
    initialMinute : Int,
    onConfirm     : (hour: Int, minute: Int) -> Unit,
    onDismiss     : () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour        = initialHour,
        initialMinute      = initialMinute,
        is24Hour           = true
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties       = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape             = RoundedCornerShape(20.dp),
            tonalElevation    = 6.dp,
            color             = MaterialTheme.colorScheme.surface,
            modifier          = Modifier
                .wrapContentSize()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier            = Modifier.padding(
                    top    = 24.dp,
                    start  = 24.dp,
                    end    = 24.dp,
                    bottom = 16.dp
                )
            ) {
                // Title
                Text(
                    text       = "Select Time",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = PrimaryBlue,
                    modifier   = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 20.dp)
                )

                // Material3 clock picker
                TimePicker(
                    state  = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor              = Color(0xFFE8EAF6),
                        clockDialSelectedContentColor = Color.White,
                        clockDialUnselectedContentColor = Color(0xFF424242),
                        selectorColor               = PrimaryBlue,
                        containerColor              = Color.White,
                        periodSelectorBorderColor   = PrimaryBlue,
                        timeSelectorSelectedContainerColor   = PrimaryBlue,
                        timeSelectorUnselectedContainerColor = Color(0xFFE8EAF6),
                        timeSelectorSelectedContentColor     = Color.White,
                        timeSelectorUnselectedContentColor   = Color(0xFF424242)
                    )
                )

                Spacer(Modifier.height(16.dp))

                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier              = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text  = "Cancel",
                            color = Color(0xFF757575)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            onConfirm(timePickerState.hour, timePickerState.minute)
                        }
                    ) {
                        Text(
                            text  = "OK",
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}