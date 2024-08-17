package com.example.flashcards.flashCards.ui.flashCardsScreen.studyScreen

import com.example.flashcards.flashCards.domain.models.FlashCard

sealed class StudyScreenState {

    data object Initial : StudyScreenState()

    data object Loading : StudyScreenState()

    data class Success(val flashCardList: List<FlashCard> = listOf(), val message: String = "") : StudyScreenState()

    data class Error(val error: String) : StudyScreenState()

}