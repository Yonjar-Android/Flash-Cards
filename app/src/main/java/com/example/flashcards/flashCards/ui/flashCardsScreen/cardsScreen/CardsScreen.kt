package com.example.flashcards.flashCards.ui.flashCardsScreen.cardsScreen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcards.R
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

    var showEditDialog by remember {
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
                Text(text = stringResource(id = R.string.strEdit), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }, onClick = {
                showDropDownMenu = false
                showEditDialog = true
            })

            DropdownMenuItem(text = {
                Text(text = stringResource(id = R.string.strDelete), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }, onClick = {
                showDropDownMenu = false
                showDeleteDialog = true
            })
        }
    }

    if (showEditDialog) {
        EditFormDialog(
            flashCard = flashCard,
            viewModel = viewModel,
            close = { showEditDialog = false })
    }

    if (showDeleteDialog) {
        DialogDelete(
            flashCard = flashCard,
            viewModel = viewModel,
            close = { showDeleteDialog = false })
    }
}

@Composable
fun DialogDelete(flashCard: FlashCard, viewModel: CardsViewModel, close: () -> Unit) {
    AlertDialog(onDismissRequest = {}, confirmButton = {
        Button(onClick = {
            viewModel.deleteFlashCard(flashCard.id)
            close()
        }) {
            Text(text = stringResource(id = R.string.strDelete))
        }
    },
        dismissButton = {
            TextButton(onClick = {
                close()
            }) {
                Text(text = stringResource(id = R.string.strCancel))

            }
        },
        title = {
            Text(text = stringResource(id = R.string.strDeleteFlashCard), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        },
        text = {
            Text(text = stringResource(id = R.string.strDeleteThisFlashCard), fontSize = 16.sp)
        })
}

@Composable
fun EditFormDialog(
    flashCard: FlashCard,
    viewModel: CardsViewModel,
    close: () -> Unit
) {

    var question by remember {
        mutableStateOf(flashCard.title)
    }
    var answer by remember {
        mutableStateOf(flashCard.answer)
    }

    AlertDialog(
        onDismissRequest = {},
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.strEditFlashCard), fontWeight = FontWeight.SemiBold)
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = {
                        Text(
                            text = stringResource(id = R.string.strQuestion),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 16.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = {
                        Text(
                            text = stringResource(id = R.string.strAnswer),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 16.sp
                    )

                )
            }
        },
        confirmButton = {
            Button(onClick = {
                viewModel.updateFlashCard(
                    flashCard = flashCard.copy(
                        title = question,
                        answer = answer
                    )
                )

                close.invoke()
            }) {
                Text(text = stringResource(id = R.string.strUpdate))
            }
        },
        dismissButton = {
            TextButton(onClick = { close.invoke() }) {
                Text(text = stringResource(id = R.string.strCancel))
            }
        }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FlashCardItem(flashCard: FlashCard, viewModel: CardsViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${stringResource(id = R.string.strQuestion)}: ${flashCard.title}",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = "${stringResource(id = R.string.strAnswer)}: ${flashCard.answer}",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
            }
            DropDownMenuComp(flashCard, viewModel = viewModel)
        }
    }

    Spacer(modifier = Modifier.size(10.dp))
}

