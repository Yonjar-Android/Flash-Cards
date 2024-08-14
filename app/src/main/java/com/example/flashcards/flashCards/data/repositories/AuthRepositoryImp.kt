package com.example.flashcards.flashCards.data.repositories

import com.example.flashcards.flashCards.domain.repositories.AuthRepository
import com.example.flashcards.flashCards.utils.CallbackHandle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthRepository {
    override suspend fun signInWithGoogle(tokenId: String, callback: CallbackHandle<Boolean>) {
        try {

            val credential = GoogleAuthProvider.getCredential(tokenId, null)

            firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                callback.onSuccess.invoke(it.isSuccessful)
            }
        } catch (e: Exception) {
            callback.onError.invoke(null)
        }

    }
}