package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cs551fitnessapp.R
import com.example.cs551fitnessapp.ui.theme.CS551FitnessAppTheme

@Composable
fun NewMemberSexScreen (modifier: Modifier = Modifier) {

}

@Composable
fun Description (modifier: Modifier = Modifier) {
    Column(modifier = Modifier) {
        Text(
            text = stringResource(R.string.tell_us_about_yourself)
        )
        Text(
            text = stringResource(R.string.Sample_Text)
        )
    }
}

val circleMod = Modifier
    .clip(CircleShape)
    .size(50.dp)
    .border(4.dp,Color.LightGray,CircleShape)
    .background(Color.LightGray)


@Composable
fun SexButton (modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .padding(24.dp)

    ) {
        Column(
            modifier
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        // Attribution for readme: <a href="https://www.flaticon.com/free-icons/male" title="male icons">Male icons created by smashingstocks - Flaticon</a>
            Image(
                painter = painterResource(id = R.drawable.male),
                contentDescription = "Male Icon",
                contentScale = ContentScale.Inside,
                modifier = circleMod
            )
            Text(
                text = "Male"
            )
        }

        Column(
            modifier
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Attribution for readme: <a href="https://www.flaticon.com/free-icons/woman" title="woman icons">Woman icons created by Freepik - Flaticon</a>
            Image(
                painter = painterResource(id = R.drawable.female),
                contentDescription = "Female Icon",
                contentScale = ContentScale.Inside,
                modifier = circleMod
            )
            Text(
                text = "Female"
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonPreview() {
    CS551FitnessAppTheme() {
        SexButton()
    }
}