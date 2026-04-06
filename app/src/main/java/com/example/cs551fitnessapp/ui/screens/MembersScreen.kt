package com.example.cs551fitnessapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.LineHeightStyle
import org.intellij.lang.annotations.JdkConstants

@Composable
fun MembersScreen(){

}
@Composable
fun SearchBar() {

}

@Composable
fun SortingButtons() {

}

@Composable
fun AddMemberButton() {

}
//drawBehind modifier taken from https://stackoverflow.com/questions/68592618/how-to-add-border-on-bottom-only-in-jetpack-compose
@Composable
fun MembersList(members : List<Member> , modifier: Modifier = Modifier) {
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally , modifier = modifier.fillMaxSize()) {
        items(items = members, key = { it.id }) { item ->
            MemberCard(member = item , modifier = Modifier
                .drawBehind {
                    val strokeWidth = 1 * density
                    val y = size.height - strokeWidth / 2
                    drawLine(
                        Color.LightGray,
                        Offset(1f, y),
                        Offset(size.width, y),
                        strokeWidth
                    )
                })

        }
    }
}
@Composable
fun MemberCard(member : Member , modifier: Modifier = Modifier) {
    Row(modifier = modifier ,) {
        MemberCardImage(modifier = Modifier.fillMaxWidth(0.3f).padding(10.dp))
        MemberCardInfo(member.name ,
            member.joinDate ,
            endDate = member.endDate,
            status = member.status,
        modifier = Modifier.fillMaxWidth(0.7f))
    }
}

@Composable
fun MemberCardImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = "Image of member",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
    )

}

@Composable
fun MemberCardInfo(name : String ,
                   joinDate : Date,
                   endDate: Date? ,
                   status : String,
                   modifier: Modifier) {
    Column(verticalArrangement = Arrangement.SpaceBetween , modifier = modifier) {
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
data class Member(val id : Int , val name : String , val joinDate : Date , val endDate : Date? , val status: String)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val members = listOf<Member>(
        Member(id = 0,
            name ="John Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Active",),
        Member( id = 1,
            name ="Mike Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Active",),
        Member(id = 2,
            name ="Major Smith" ,
            joinDate = Date(2026 , 3 , 17) ,
            endDate = null ,
            status = "Active",)
    )
    CS551FitnessAppTheme {
        MembersList(members)
    }
}