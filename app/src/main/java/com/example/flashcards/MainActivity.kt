package com.example.flashcards

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flashcards.flashCards.ui.flashCardsScreen.FlashCardsScreen
import com.example.flashcards.flashCards.ui.flashCardsScreen.cardsScreen.CardsViewModel
import com.example.flashcards.flashCards.ui.flashCardsScreen.studyScreen.StudyScreenViewModel
import com.example.flashcards.flashCards.ui.initialScreen.InitialScreen
import com.example.flashcards.flashCards.ui.initialScreen.InitialScreenViewModel
import com.example.flashcards.ui.theme.FlashCardsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val initialScreenViewModel: InitialScreenViewModel by viewModels()

    private val studyScreenViewModel: StudyScreenViewModel by viewModels()

    private val cardsScreenViewModel: CardsViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            false
        }

        enableEdgeToEdge()



        setContent {
            FlashCardsTheme {
                val loading = initialScreenViewModel.isLoading.collectAsState()

                val navController = rememberNavController()

                if (loading.value) {
                    LoadingScreen()
                } else {


                    NavHost(
                        navController = navController,
                        startDestination = if (initialScreenViewModel.isLoggedIn) {
                            "FlashCardsScreen"
                        } else {
                            "InitialScreen"
                        }
                    ) {
                        composable("InitialScreen") {
                            InitialScreen(
                                viewModel = initialScreenViewModel,
                                navHostController = navController
                            )
                        }

                        composable("FlashCardsScreen") {
                            FlashCardsScreen(studyScreenViewModel, cardsScreenViewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        Modifier
            .background(Color.White)
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
