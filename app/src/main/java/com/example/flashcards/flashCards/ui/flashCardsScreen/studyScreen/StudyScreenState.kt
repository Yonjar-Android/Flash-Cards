package com.example.flashcards.flashCards.ui.flashCardsScreen.studyScreen

sealed class StudyScreenState {

    data object Initial : StudyScreenState()

    data object Loading : StudyScreenState()

    data class Success(val message: String = "") : StudyScreenState()

    data class Error(val error: String) : StudyScreenState()

}