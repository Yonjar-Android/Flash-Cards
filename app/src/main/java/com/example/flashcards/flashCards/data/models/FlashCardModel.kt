package com.example.flashcards.flashCards.data.models

import com.example.flashcards.flashCards.domain.models.FlashCard
import com.google.firebase.firestore.PropertyName

data class FlashCardModel(
    @PropertyName("flashCardId") val id:String = "",
    @PropertyName("title") val title:String = "",
    @PropertyName("answer") val answer:String = "",
    @PropertyName("nextReview") val nextReview:Long = 0,
    @PropertyName("isLearned") val isLearned:Boolean = false
){
    fun toFlashCardDomain():FlashCard{
       return FlashCard(
            id = id,
            title = title,
            answer = answer,
            nextReview = nextReview
        )
    }
}