package com.example.flashcards.flashCards.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.flashcards.R

@Composable
fun InitialScreen() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xffD6D6D6))
    ) {

        val (buttons, logo, title) = createRefs()

        val guidelineBottom = createGuidelineFromBottom(0.10f)
        val guidelineTop = createGuidelineFromTop(0.10f)

        Text(text = "Flash Cards App", fontWeight = FontWeight.Bold, fontSize = 48.sp,
            modifier = Modifier.constrainAs(title){
                top.linkTo(guidelineTop)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            })

        Image(painter = painterResource(id = R.drawable.flashcards_logo), contentDescription = "App logo",
            modifier = Modifier.constrainAs(logo){
                top.linkTo(title.bottom)
                bottom.linkTo(buttons.top)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }.size(270.dp))
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
                .constrainAs(buttons) {
                    bottom.linkTo(guidelineBottom)
                }
        ) {
            AuthButton("Login")
            Spacer(modifier = Modifier.height(20.dp))
            AuthButton("Register")
        }
    }
}

@Composable
fun AuthButton(text: String) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .height(80.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff052A62)),
        onClick = {

        }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(4f)
            )

            Image(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = "Google Logo",
                modifier = Modifier
                    .weight(1f)
                    .size(45.dp),
            )
        }
    }
}