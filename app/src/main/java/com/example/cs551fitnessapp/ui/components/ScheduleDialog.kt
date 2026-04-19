package com.example.cs551fitnessapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.cs551fitnessapp.ui.viewmodels.SessionTimeResult

@Composable
fun ScheduleDialog(
    results   : List<SessionTimeResult>,
    onDismiss : () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape  = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text       = "Sessions Schedule",
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                if (results.isEmpty()) {
                    Text("No sessions found.", color = Color.Gray, fontSize = 14.sp)
                } else {
                    results.forEach { result ->
                        Card(
                            shape  = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text       = result.sessionName,
                                    fontSize   = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color      = Color(0xFF212121)
                                )
                                Spacer(Modifier.height(4.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("${result.startTime} → ${result.endTime}", fontSize = 13.sp, color = Color(0xFF2962FF))

                                }
                            }
                        }
                    }
                }

                Button(
                    onClick  = onDismiss,
                    shape    = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("OK")
                }
            }
        }
    }
}