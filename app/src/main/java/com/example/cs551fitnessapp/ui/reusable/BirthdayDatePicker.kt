package com.example.cs551fitnessapp.ui.reusable

import android.app.DatePickerDialog

import androidx.compose.material3.OutlinedButton

import androidx.compose.material3.Text

import androidx.compose.runtime.*

import androidx.compose.ui.platform.LocalContext

import java.util.Calendar



@Composable
fun BirthdayDatePicker(

    onDateSelected: (String) -> Unit

) {

    val context = LocalContext.current


    val calendar = Calendar.getInstance()


    val year = calendar.get(Calendar.YEAR)

    val month = calendar.get(Calendar.MONTH)

    val day = calendar.get(Calendar.DAY_OF_MONTH)



    var selectedDate by remember {

        mutableStateOf("")

    }



    val datePickerDialog = DatePickerDialog(

        context,

        { _, selectedYear, selectedMonth, selectedDay ->


            val formattedDate =

                "${selectedDay}/${selectedMonth + 1}/${selectedYear}"


            selectedDate = formattedDate


            onDateSelected(formattedDate)

        },

        year,

        month,

        day

    )



    OutlinedButton(

        onClick = {

            datePickerDialog.show()

        }

    ) {

        if (selectedDate.isEmpty()) {

            Text("Select Date from Calendar")

        }

        else {

            Text(selectedDate)

        }

    }

}