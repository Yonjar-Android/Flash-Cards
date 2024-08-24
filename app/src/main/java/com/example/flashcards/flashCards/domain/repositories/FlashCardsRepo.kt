package com.example.flashcards.flashCards.domain.repositories

import com.example.flashcards.flashCards.data.models.FlashCardModel
import com.example.flashcards.flashCards.data.repositories.ResultFlashCard
import com.example.flashcards.flashCards.domain.models.FlashCard

interface FlashCardsRepo {

    suspend fun getFlashCards(): ResultFlashCard<List<FlashCard>>

    fun convertToFlashCardDomainModel(oldList: List<FlashCardModel>):List<FlashCard>

    suspend fun createFlashCard(concept: String, response: String): ResultFlashCard<String>

    suspend fun updateFlashCardNextReview(flashCard: FlashCard): ResultFlashCard<String>

    suspend fun updateFlashCard(flashCard: FlashCard): ResultFlashCard<String>

    suspend fun deleteFlashCard(flashCardId:String): ResultFlashCard<String>

}