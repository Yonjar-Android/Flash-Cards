package com.example.flashcards.flashCards.data.repositories

sealed class ResultFlashCard<out T> {
    data class Success<out T>(val data:T): ResultFlashCard<T>()

    data class Error(val error:String): ResultFlashCard<Nothing>()
}