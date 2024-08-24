package com.example.flashcards.flashCards.ui.flashCardsScreen.cardsScreen

sealed class CardsScreenState {

    data object Initial:CardsScreenState()

    data object Loading:CardsScreenState()

    data class Error(val error:String):CardsScreenState()

    data class Success(val message:String): CardsScreenState()

}