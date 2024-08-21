package com.example.flashcards.motherObject

import com.example.flashcards.flashCards.data.repositories.ResultFlashCard
import com.example.flashcards.flashCards.domain.models.FlashCard

object FlashCardMotherObject {

     val flashCard = FlashCard(
        id = "1",
        title = "Tree",
        answer = "Arbol",
        nextReview = 60000,
        )

     val flashCardsList = listOf(
        flashCard,
        FlashCard(
            id = "2",
            title = "Two",
            answer = "Dos",
            nextReview = 60000,

            ),
        FlashCard(
            id = "3",
            title = "Apple",
            answer = "Manzana",
            nextReview = 60000,
            )
    )

    val resultFlashCardSuccess = ResultFlashCard.Success(data = flashCardsList)

    val resultFlashCardSuccessEmpty = ResultFlashCard.Success(data = listOf<FlashCard>())

    val resultFlashCardError = ResultFlashCard.Error(error = "An error has occurred")

    private val msg = "Success Message"

    val resultFlashCardSuccessCreationMsg = ResultFlashCard.Success(data = msg)


}