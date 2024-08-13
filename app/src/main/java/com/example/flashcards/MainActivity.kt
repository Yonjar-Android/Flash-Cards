package com.example.flashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flashcards.flashCards.ui.InitialScreen
import com.example.flashcards.ui.theme.FlashCardsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashCardsTheme {
                InitialScreen()

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "InitialScreen" ){
                    composable("InitialScreen"){
                        InitialScreen()
                    }

                    composable("FlashCardsScreen"){

                    }
                }

            }
        }
    }
}
