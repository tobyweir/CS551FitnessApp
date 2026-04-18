package com.example.cs551fitnessapp.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun AddMemberNameScreen(
    name: String,
    sessions: String,
    onNameChange: (String) -> Unit,
    onSessionsChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    enableSaveButton: Boolean,
    isSessionError: Boolean,
    isNameError: Boolean,
) {

    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

    // stores selected image
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    // gallery launcher
    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            selectedImageUri = uri
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                "What's your name?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(20.dp))

            // PROFILE IMAGE
            if (selectedImageUri != null) {

                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "profile image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        }
                )

            } else {

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "profile placeholder",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        }
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                "Edit",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.clickable {
                    imagePickerLauncher.launch("image/*")
                }
            )

            Spacer(Modifier.height(20.dp))

            // NAME FIELD
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                placeholder = {
                    Text("Full name")
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(
                            androidx.compose.ui.focus.FocusDirection.Down
                        )
                    }
                ),
                isError = isNameError,
                supportingText = {
                    if (isNameError) {
                        Text(
                            "Please enter a name",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            // SESSION FIELD
            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    "Num Of Session :",
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.width(10.dp))

                OutlinedTextField(
                    value = sessions,
                    onValueChange = onSessionsChange,
                    singleLine = true,
                    isError = isSessionError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (enableSaveButton) {
                                onSaveClick()
                            }
                        }
                    ),
                    supportingText = {
                        if (isSessionError) {
                            Text(
                                "Enter session count",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
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
            Text("Save", color = Color.White)
        }
    }
}