package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cs551fitnessapp.Greeting
import com.example.cs551fitnessapp.R
import com.example.cs551fitnessapp.ui.theme.CS551FitnessAppTheme
import java.util.Date

@Composable
fun SearchBar() {

}

@Composable
fun SortingButtons() {

}

@Composable
fun AddMemberButton() {

}
@Composable
fun MemberCard(member : Member , modifier: Modifier = Modifier) {
    Row() {
        MemberCardImage(modifier = Modifier)
        MemberCardInfo("test" ,
            Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Active",
        modifier = Modifier)
    }
}

@Composable
fun MemberCardImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = "Image of member",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
    )

}

@Composable
fun MemberCardInfo(name : String ,
                   joinDate : Date,
                   endDate: Date? ,
                   status : String,
                   modifier: Modifier) {
    Column() {
        Text(text = name)
        Text(text = "Join : ${joinDate.date}/${joinDate.month}/${joinDate.year}")
        if (endDate != null) {
            Text(text = "End Session : ${endDate.date}/${endDate.month}/${endDate.year}")
        } else {
            Text(text = "End Session : - ")
        }
        Card() {
            Text(text = status , modifier = Modifier
                .padding (top = 1.dp , start = 10.dp , end = 10.dp))
        }
    }
}
//Temporary until database has these entities
data class Member(val name : String ,)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CS551FitnessAppTheme {
        MemberCard(Member("John Smith"))
    }
}