package com.example.cs551fitnessapp.ui.reusable

import androidx.compose.material3.R
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource


@Composable
fun ChoiceButton (optionOne: Int, optionTwo: Int, modifier: Modifier = Modifier) {
    var selectedChoice by remember { mutableIntStateOf(0) }
    var options = listOf(optionOne, optionTwo)

    SingleChoiceSegmentedButtonRow {
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
}