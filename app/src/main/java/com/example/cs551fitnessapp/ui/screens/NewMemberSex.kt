package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cs551fitnessapp.R
import com.example.cs551fitnessapp.ui.reusable.Description
import com.example.cs551fitnessapp.ui.reusable.NextScreenButton
import com.example.cs551fitnessapp.ui.theme.CS551FitnessAppTheme
import com.example.cs551fitnessapp.ui.viewmodels.AddMemberViewModel

@Composable
fun NewMemberSexScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNextClick: (String) -> Unit = {},
    enableNextButton: Boolean,
    selectedSex: String,
    updateSex : (String) -> Unit = {}
) {

    Column(
        modifier
            .padding(20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Description(R.string.tell_us_about_yourself, R.string.Sample_Text)

        SexSelector(
            selectedSex = selectedSex,
            onSexSelected = updateSex
        )

        SubmissionButtons(
            onPreferNotToSayClick = {
                updateSex("Prefer not to say")
                onNextClick("Prefer not to say")
            },
            onNextClick = {
                if (selectedSex.isNotBlank()) {
                    onNextClick(selectedSex)
                }
            },
            enableNextButton = enableNextButton
        )
    }
}

val circleMod = Modifier
    .clip(CircleShape)
    .size(100.dp)
    .border(4.dp, Color.LightGray, CircleShape)
    .background(Color.LightGray)

val circleModFilled = Modifier
    .clip(CircleShape)
    .size(100.dp)
    .border(4.dp, Color.DarkGray, CircleShape)
    .background(Color.DarkGray)

@Composable
fun SexSelector(
    selectedSex: String,
    onSexSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            IconButton(
                onClick = { onSexSelected("Male") },
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.male),
                    contentDescription = "Male Button",
                    contentScale = ContentScale.Inside,
                    modifier = if (selectedSex == "Male") circleModFilled else circleMod
                )
            }
            Text(
                text = "Male",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Column {
            IconButton(
                onClick = { onSexSelected("Female") },
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.female),
                    contentDescription = "Female Button",
                    contentScale = ContentScale.Inside,
                    modifier = if (selectedSex == "Female") circleModFilled else circleMod
                )
            }
            Text(
                text = "Female",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun SubmissionButtons(
    modifier: Modifier = Modifier,
    onPreferNotToSayClick: () -> Unit,
    onNextClick: () -> Unit,
    enableNextButton : Boolean
) {
    Column(modifier) {
        OutlinedButton(
            onClick = onPreferNotToSayClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Prefer not to say")
        }

        NextScreenButton(onNextClick , enabled = enableNextButton)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ButtonPreview() {
  //  CS551FitnessAppTheme {
    //    NewMemberSexScreen()
    //}
//}