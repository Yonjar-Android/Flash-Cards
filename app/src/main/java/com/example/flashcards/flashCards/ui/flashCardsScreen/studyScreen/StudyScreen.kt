package com.example.flashcards.flashCards.ui.flashCardsScreen.studyScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcards.R

@Composable
fun StudyScreen() {

    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Cartas por estudiar N°:", fontWeight = FontWeight.Bold, fontSize = 32.sp,
            lineHeight = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 35.dp)
        )

        Spacer(modifier = Modifier.size(50.dp))

        Image(
            modifier = Modifier.size(250.dp),
            painter = painterResource(id = R.drawable.flashcards_image),
            contentDescription = "Flash Cards image"
        )

        Spacer(modifier = Modifier.size(50.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            onClick = {

            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xff072ECD))
        ) {
            Text(text = "Estudiar", fontWeight = FontWeight.Bold, fontSize = 32.sp)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp, bottom = 20.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FabAddButton {

        }
    }
}

@Composable
fun FabAddButton(showCard: () -> Unit) {

    FloatingActionButton(
        containerColor = Color.Red,
        onClick = {
            showCard()
        }) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Card", tint = Color.White)

    }
}