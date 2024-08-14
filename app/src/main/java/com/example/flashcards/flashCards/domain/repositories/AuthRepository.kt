package com.example.flashcards.flashCards.domain.repositories

import com.example.flashcards.flashCards.utils.CallbackHandle

interface AuthRepository {

    suspend fun signInWithGoogle(tokenId:String, callback: CallbackHandle<Boolean>)

}