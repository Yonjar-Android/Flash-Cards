package com.example.flashcards.flashCards.ui.flashCardsScreen.studyScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcards.R
import com.example.flashcards.flashCards.domain.models.FlashCard

@Composable
fun StudyScreen(studyScreenViewModel: StudyScreenViewModel) {

    val state = studyScreenViewModel.state.collectAsState()

    var flashCards = studyScreenViewModel.flashcards.collectAsState()

    val context = LocalContext.current

    var show by rememberSaveable {
        mutableStateOf(false)
    }

    var showFlashCards by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        studyScreenViewModel.getFlashCards()
    }



    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Cartas por estudiar N°: ${flashCards.value.size}",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
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

                showFlashCards = true

            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xff072ECD)),
            enabled = flashCards.value.isNotEmpty()
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
        FabAddButton { show = true }
    }


    /* Si el valor de la variable show es true, entonces se muestra la pantalla para crear
    una nueva flashCard */

    if (show) {
        DialogAddCards(studyScreenViewModel, close = { show = !show })
    }

    if (showFlashCards) {
        FlashCards(
            studyScreenViewModel = studyScreenViewModel,
            flashCards.value,
            close = { showFlashCards = !showFlashCards }
        )
    }

    /* Verificamos el valor del estado del viewModel y acorde a ello realizamos ciertas
     acciones a mostrar en la pantalla */

    when (val currentState = state.value) {
        is StudyScreenState.Error -> {
            Toast.makeText(context, currentState.error, Toast.LENGTH_LONG).show()
            studyScreenViewModel.resetState()
        }

        StudyScreenState.Initial -> {}

        StudyScreenState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(36.dp))
                        .padding(8.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
        }

        is StudyScreenState.Success -> {
            if (currentState.message.isNotBlank()) {
                Toast.makeText(context, currentState.message, Toast.LENGTH_LONG).show()
            }
            studyScreenViewModel.resetState()
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


@Composable
fun DialogAddCards(
    studyScreenViewModel: StudyScreenViewModel,
    close: () -> Unit,
) {

    var name by remember {
        mutableStateOf("")
    }

    var answer by remember {
        mutableStateOf("")
    }

    Box(
        Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(30.dp))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = {
                    close()
                }) {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = Icons.Filled.Clear, contentDescription = "Close Icon"
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Crear flash card",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold
                )

                DefectSpacer()

                TextFieldComponent("Concepto", name) { name = it }

                DefectSpacer()

                TextFieldComponent("Respuesta", answer) { answer = it }

                DefectSpacer()

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    onClick = {
                        studyScreenViewModel.createFlashCard(name, answer)

                        close.invoke()
                    }) {
                    Text(
                        text = "Crear",
                        fontSize = 32.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TextFieldComponent(labelText: String, actualValue: String, onChangeValue: (String) -> Unit) {
    Spacer(modifier = Modifier.size(10.dp))

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = actualValue,
        onValueChange = { onChangeValue(it) },
        maxLines = 1,
        singleLine = true,
        shape = RoundedCornerShape(20.dp),
        label = {
            Text(
                text = labelText,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
            )

        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}


@Composable
fun FlashCards(
    studyScreenViewModel: StudyScreenViewModel,
    flashCards: List<FlashCard>,
    close: () -> Unit
) {

    val context = LocalContext.current

    var currentIndex = 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (flashCards.isNotEmpty()) {
            FlashCardItem(
                studyScreenViewModel = studyScreenViewModel,
                flashCard = flashCards[currentIndex],
                onSwiped = { lambda ->
                    if (flashCards.size == 1) {
                        lambda(flashCards[0])
                    } else {
                        lambda(flashCards[1])
                    }
                })
        } else {
            close.invoke()
        }

    }
}

@Composable
fun FlashCardItem(
    studyScreenViewModel: StudyScreenViewModel,
    flashCard: FlashCard,
    onSwiped: ((FlashCard) -> Unit) -> Unit
) {

    val showAnswer = "Show answer"
    val showQuestion = "Show question"

    var textValue by rememberSaveable {
        mutableStateOf(flashCard.title)
    }

    var textValueButton by rememberSaveable {
        mutableStateOf(showAnswer)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {

        Text(
            text = textValue,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        DefectSpacer()

        Button(onClick = {
            when (textValueButton) {
                "Show answer" -> {
                    textValueButton = showQuestion
                    textValue = flashCard.answer
                }

                "Show question" -> {
                    textValueButton = showAnswer
                    textValue = flashCard.title
                }
            }

        }) {
            Text(
                text = textValueButton,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        DefectSpacer()

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                updateNextReview(
                    studyScreenViewModel = studyScreenViewModel,
                    flashCard = flashCard,
                    interval = 1 * 60 * 1000
                ) // 1 minuto
                onSwiped() { flashCard ->
                    textValue = flashCard.title
                    textValueButton = showAnswer
                }
            }) {
                Text("1 Minuto")
            }

            Button(onClick = {
                updateNextReview(
                    studyScreenViewModel = studyScreenViewModel,
                    flashCard = flashCard,
                    interval = 5 * 60 * 1000
                ) // 5 minutos
                onSwiped() { flashCard ->
                    textValue = flashCard.title
                    textValueButton = showAnswer
                }
            }) {
                Text("5 Minutos")
            }

            Button(onClick = {
                updateNextReview(
                    studyScreenViewModel = studyScreenViewModel,
                    flashCard = flashCard,
                    interval = 24 * 60 * 60 * 1000
                ) // 1 día
                onSwiped() { flashCard ->
                    textValue = flashCard.title
                    textValueButton = showAnswer
                }
            }) {
                Text("1 Día")
            }

            Button(onClick = {
                updateNextReview(
                    studyScreenViewModel = studyScreenViewModel,
                    flashCard = flashCard,
                    interval = 7 * 24 * 60 * 60 * 1000
                ) // 1 semana
                onSwiped() { flashCard ->
                    textValue = flashCard.title
                    textValueButton = showAnswer
                }
            }) {
                Text("1 Semana")
            }
        }
    }
}

@Composable
fun DefectSpacer() {
    Spacer(modifier = Modifier.size(30.dp))
}

fun updateNextReview(
    studyScreenViewModel: StudyScreenViewModel,
    flashCard: FlashCard,
    interval: Long
) {
    val nextReview = System.currentTimeMillis() + interval

    studyScreenViewModel.updateFlashCardReview(
        flashCard = flashCard.copy(
            nextReview = nextReview
        )
    )

}

