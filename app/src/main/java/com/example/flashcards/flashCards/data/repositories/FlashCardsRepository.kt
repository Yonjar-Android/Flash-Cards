package com.example.flashcards.flashCards.data.repositories

import com.example.flashcards.R
import com.example.flashcards.flashCards.data.models.FlashCardModel
import com.example.flashcards.flashCards.domain.models.FlashCard
import com.example.flashcards.flashCards.domain.repositories.FlashCardsRepo
import com.example.flashcards.flashCards.utils.ResourceProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FlashCardsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val resourceProvider: ResourceProvider
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
                            continuation.resume(ResultFlashCard.Error(resourceProvider.getString(R.string.strNoFlashCardsToStudy)))
                        } else {
                            val newFlashCards = convertToFlashCardDomainModel(flashCards)
                            continuation.resume(ResultFlashCard.Success(newFlashCards))
                        }

                    } else {
                        continuation.resume(ResultFlashCard.Error(resourceProvider.getString(R.string.strErrorGettingCards)))
                    }
                }.addOnFailureListener { e ->
                    continuation.resume(ResultFlashCard.Error(e.message ?: ""))
                }
        }
    }

    override fun convertToFlashCardDomainModel(oldList: List<FlashCardModel>): List<FlashCard> {
        val newList: MutableList<FlashCard> = mutableListOf()

        for (i in oldList) {

            /* Verifica que si el tiempo de revisión es ahora o ya ha pasado
             entonces que agregue la flashcard para estudiarla */

            if (i.nextReview <= System.currentTimeMillis()) {
                newList.add(i.toFlashCardDomain())
            }

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
                    continuation.resume(ResultFlashCard.Success(resourceProvider.getString(R.string.strFlashCardCreatedSuccessfully)))
                }.addOnFailureListener { e ->
                    continuation.resume(ResultFlashCard.Error(e.message ?: ""))
                }
        }
    }

    override suspend fun updateFlashCardNextReview(flashCard: FlashCard): ResultFlashCard<String> {

        val updateFlashCard: HashMap<String, Any> = hashMapOf("nextReview" to flashCard.nextReview)

        return suspendCancellableCoroutine { continuation ->
            firestore.collection("FlashCards").document(flashCard.id)
                .update(updateFlashCard)
                .addOnSuccessListener {
                    continuation.resume(ResultFlashCard.Success(""))
                }
                .addOnFailureListener {
                    continuation.resume(ResultFlashCard.Error(it.message ?: ""))
                }
        }
    }

    override suspend fun updateFlashCard(flashCard: FlashCard): ResultFlashCard<String> {
        val updateFlashCard: HashMap<String, Any> = hashMapOf(
            "title" to flashCard.title,
            "answer" to flashCard.answer
        )

        return suspendCancellableCoroutine { continuation ->

            firestore.collection("FlashCards").document(flashCard.id)
                .update(updateFlashCard)
                .addOnSuccessListener {
                    continuation.resume(ResultFlashCard.Success(resourceProvider.getString(R.string.strFlashCardUpdatedSuccessfully)))
                }
                .addOnFailureListener {
                    continuation.resume(ResultFlashCard.Error(it.message ?: ""))
                }
        }
    }

    override suspend fun deleteFlashCard(flashCardId: String): ResultFlashCard<String> {
        return suspendCancellableCoroutine { continuation ->

            firestore.collection("FlashCards").document(flashCardId).delete()
                .addOnSuccessListener {
                    continuation.resume(ResultFlashCard.Success(resourceProvider.getString(R.string.strFlashCardDeletedSuccessfully)))
                }.addOnFailureListener {
                    continuation.resume(ResultFlashCard.Error(it.message ?: ""))
                }
        }
    }
}