package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cs551fitnessapp.R
import com.example.cs551fitnessapp.ui.theme.CS551FitnessAppTheme

@Composable
fun AddMemberNameScreen(
    name: String,
    sessions: String,
    onNameChange: (String) -> Unit,
    onSessionsChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    enableSaveButton : Boolean,
    isSessionError : Boolean,
    isNameError : Boolean,
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


//            Spacer(Modifier.height(10.dp))

            Text(
                "What is the new member's name?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(20.dp))

            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "profile",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable { }
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Add profile picture",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { onNameChange(it) },
                placeholder = {
                    Text("Full name")
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(focusDirection = FocusDirection.Down)}),
                isError = isNameError,
                supportingText = {if (isNameError) {
                    Text("Please enter a name" , color = MaterialTheme.colorScheme.error)
                } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Number of Sessions",
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.width(10.dp))

                OutlinedTextField(
                    value = sessions,
                    onValueChange = { onSessionsChange(it) },
                    singleLine = true,
                    isError = isSessionError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number , imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {if (enableSaveButton) {
                        onSaveClick()
                    } }),
                    supportingText = {if (isSessionError) {
                        Text("Please enter a session count" , color = MaterialTheme.colorScheme.error)
                    } },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.width(100.dp)
                )
            }
        }

        Button(
            onClick = onSaveClick,
            enabled = enableSaveButton,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text("Save" , color = Color.White)

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun AddMemberNamePreview() {
//    CS551FitnessAppTheme {
//        AddMemberNameScreen(
//            name = "",
//            sessions = "20",
//            onNameChange = {},
//            onSessionsChange = {},
//            onBackClick = {},
//            onSaveClick = {}
//        )
//    }
//}