package com.example.flashcards.flashCards.ui.flashCardsScreen.cardsScreen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcards.flashCards.domain.models.FlashCard

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardsScreen(cardsScreenViewModel: CardsViewModel) {

    LaunchedEffect(Unit) {
        cardsScreenViewModel.getFlashCards()
    }

    val context = LocalContext.current
    val state = cardsScreenViewModel.state.collectAsState()

    val flashCards = cardsScreenViewModel.flashCards.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(flashCards.value) { flashCard ->
            FlashCardItem(flashCard = flashCard, viewModel = cardsScreenViewModel)
        }
    }

    when (val currentState = state.value) {
        is CardsScreenState.Error -> {
            Toast.makeText(context, "Error: ${currentState.error}", Toast.LENGTH_LONG).show()
            cardsScreenViewModel.resetState()
        }

        CardsScreenState.Initial -> {}
        CardsScreenState.Loading -> {
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

        is CardsScreenState.Success -> {
            if (currentState.message.isNotBlank()) {
                Toast.makeText(context, currentState.message, Toast.LENGTH_LONG).show()
            }
            cardsScreenViewModel.resetState()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DropDownMenuComp(flashCard: FlashCard, viewModel: CardsViewModel) {

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var showDropDownMenu by remember {
        mutableStateOf(false)
    }

    Box {
        Icon(
            imageVector = Icons.Filled.MoreVert, contentDescription = "Three points icon",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    showDropDownMenu = true
                })

        DropdownMenu(
            modifier = Modifier.align(Alignment.TopEnd),
            expanded = showDropDownMenu, onDismissRequest = { showDropDownMenu = false }) {

            DropdownMenuItem(text = {
                Text(text = "Editar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }, onClick = {
                showDropDownMenu = false

            })

            DropdownMenuItem(text = {
                Text(text = "Eliminar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }, onClick = {
                showDropDownMenu = false
                showDeleteDialog = true
            })
        }
    }

    if (showDeleteDialog){
        DialogDelete(flashCard = flashCard, viewModel = viewModel,close = { showDeleteDialog = false})
    }
}

@Composable
fun DialogDelete(flashCard: FlashCard, viewModel: CardsViewModel, close: () -> Unit) {
    AlertDialog(onDismissRequest = {}, confirmButton = {
        TextButton(onClick = {
            viewModel.deleteFlashCard(flashCard.id)
            close()
        }) {
            Text(text = "Eliminar")
        }
    },
        dismissButton = {
            TextButton(onClick = {
                close()
            }) {
                Text(text = "Cancelar")

            }
        },
        title = {
            Text(text = "Eliminar flash card")
        },
        text = {
            Text(text = "¿Deseas eliminar esta flash card?")
        })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FlashCardItem(flashCard: FlashCard, viewModel: CardsViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Question: ${flashCard.title}",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = "Answer: ${flashCard.answer}",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
            }
            DropDownMenuComp(flashCard, viewModel = viewModel)
        }
    }

    Spacer(modifier = Modifier.size(10.dp))
}

