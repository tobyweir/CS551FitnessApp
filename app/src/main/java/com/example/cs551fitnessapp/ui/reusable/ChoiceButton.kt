package com.example.cs551fitnessapp.ui.reusable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.R
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun ChoiceButton (optionOne: Int, optionTwo: Int, modifier: Modifier = Modifier) {
    var selectedChoice by remember { mutableIntStateOf(0) }
    var options = listOf(optionOne, optionTwo)
    var text by remember { mutableStateOf("") }

    Column {
        SingleChoiceSegmentedButtonRow(
            modifier
                .fillMaxWidth()
        ) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    onClick = { selectedChoice = index },
                    selected = index == selectedChoice,
                    label = {
                        when (label) {
                            optionOne -> Text(
                                stringResource(optionOne)
                            )

                            optionTwo -> Text(
                                stringResource(optionTwo)
                            )
                        }
                    }
                )
            }
        }
        Row {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                keyboardOptions = KeyboardOptions (
                    keyboardType = KeyboardType.Number
                ),
                label = {
                    when (selectedChoice) {
                        0 -> Text(
                            stringResource(optionOne)
                        )
                        1 -> Text(
                            stringResource(optionTwo)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}