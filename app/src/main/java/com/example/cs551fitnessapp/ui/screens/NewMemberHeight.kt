package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cs551fitnessapp.R
import com.example.cs551fitnessapp.ui.reusable.Description
import com.example.cs551fitnessapp.ui.reusable.NextScreenButton
import com.example.cs551fitnessapp.ui.theme.CS551FitnessAppTheme

@Composable
fun HeightScreen(
    height: String,
    heightUnit: String,
    onHeightChange: (String) -> Unit,
    onHeightUnitChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
    isError : Boolean,
    enableNextButton : Boolean,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Description(R.string.Height, R.string.Height2)

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = 0,
                        count = 2
                    ),
                    onClick = { onHeightUnitChange("cm") },
                    selected = heightUnit == "cm",
                    label = {
                        Text("cm")
                    }
                )

                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = 1,
                        count = 2
                    ),
                    onClick = { onHeightUnitChange("ft") },
                    selected = heightUnit == "ft",
                    label = {
                        Text("ft")
                    }
                )
            }

            OutlinedTextField(
                value = height,
                onValueChange = { onHeightChange(it) },
                label = {
                    Text(heightUnit)
                },
                placeholder = {
                    Text("Enter height")
                },
                keyboardActions = KeyboardActions(onDone = {if (enableNextButton) {
                    onNextClick()
                } }),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number , imeAction = ImeAction.Done),
                isError = isError,
                supportingText = {if(isError) {
                    Text("Please enter a valid number" , color = MaterialTheme.colorScheme.error)
                } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        NextScreenButton(
            onNextClick,
            enabled = enableNextButton,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HeightPreview() {
//    CS551FitnessAppTheme {
//        HeightScreen(
//            height = "",
//            heightUnit = "cm",
 //           onHeightChange = {},
//            onHeightUnitChange = {},
//            onBackClick = {},
//            onNextClick = {}
//        )
//    }
//}