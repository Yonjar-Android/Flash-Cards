package com.example.flashcards.flashCards.data.repositories

import com.example.flashcards.flashCards.data.models.FlashCardModel
import com.example.flashcards.flashCards.domain.models.FlashCard
import com.example.flashcards.flashCards.domain.repositories.FlashCardsRepo
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FlashCardsRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) :
    FlashCardsRepo {
    override suspend fun getFlashCards(): ResultFlashCard<List<FlashCard>> {
        return suspendCoroutine { continuation ->
            firestore.collection("FlashCards").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val flashCards =
                            task.result?.toObjects(FlashCardModel::class.java) ?: emptyList()
                        if (flashCards.isEmpty()) {
                            continuation.resume(ResultFlashCard.Error("No hay flashcards para estudiar"))
                        } else {
                            val newFlashCards = convertToFlashCardDomainModel(flashCards)
                            continuation.resume(ResultFlashCard.Success(newFlashCards))
                        }

                    } else {
                        continuation.resume(ResultFlashCard.Error("Error al obtener las flashcards"))
                    }
                }.addOnFailureListener { e ->
                    continuation.resume(ResultFlashCard.Error(e.message ?: ""))
                }
        }
    }

    override fun convertToFlashCardDomainModel(oldList: List<FlashCardModel>): List<FlashCard> {
        val newList: MutableList<FlashCard> = mutableListOf()

        for (i in oldList) {
            newList.add(i.toFlashCardDomain())
        }

        return newList
    }

    override suspend fun createFlashCard(
        concept: String,
        response: String
    ): ResultFlashCard<String> {
        return suspendCoroutine { continuation ->

            val flashCardRef = firestore.collection("FlashCards").document()

            val flashCard = FlashCardModel(
                id = flashCardRef.id,
                title = concept,
                answer = response,
                nextReview = System.currentTimeMillis(),
            )

            flashCardRef.set(flashCard)
                .addOnSuccessListener {
                    continuation.resume(ResultFlashCard.Success("Flashcard creada con éxito"))
                }.addOnFailureListener { e ->
                    continuation.resume(ResultFlashCard.Error(e.message ?: ""))
                }
        }
    }
}