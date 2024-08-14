package com.example.flashcards.flashCards.ui.flashCardsScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flashcards.R
import com.example.flashcards.flashCards.ui.flashCardsScreen.cardsScreen.CardsScreen
import com.example.flashcards.flashCards.ui.flashCardsScreen.studyScreen.StudyScreen

@Composable
fun FlashCardsScreen() {

    val navigationController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigation(navigationController)
        },
        content = {
            NavHost(
                modifier = Modifier.padding(it),
                navController = navigationController,
                startDestination = "StudyScreen"
            ) {

                composable("StudyScreen") {
                    StudyScreen()
                }

                composable("CardsScreen") {
                    CardsScreen()
                }

            }
        })
}

@Composable
fun BottomNavigation(navHostController: NavHostController) {

    var navIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        NavigationBarItem(selected = navIndex == 0, onClick = {
            navIndex = 0
            navHostController.navigate("StudyScreen")
        }, icon = {
            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.studyicon),
                contentDescription = "Study Icon"
            )
        },
            label = {
                Text(text = "Study")
            })

        NavigationBarItem(
            selected = navIndex == 1,
            onClick = {
                navIndex = 1
                navHostController.navigate("CardsScreen")
            },
            icon = {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.cards),
                    contentDescription = "Cards Icon"
                )
            },
            label = {
                Text(text = "Flash Cards")
            },
        )

    }
}