package com.example.flashcards.flashCards.domain.models

data class FlashCard(
    val id:String,
    val title:String,
    val answer:String,
    val nextReview:Long
)