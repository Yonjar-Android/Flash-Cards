package com.example.flashcards.flashCards.ui.initialScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.flashCards.data.repositories.AuthRepositoryImp
import com.example.flashcards.flashCards.utils.CallbackHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitialScreenViewModel @Inject constructor(private val authRepositoryImp: AuthRepositoryImp) :
    ViewModel() {

    fun loginWithGoogleAccount(tokenId: String, authResult: (Boolean) -> Unit) {

        viewModelScope.launch {
            authRepositoryImp.signInWithGoogle(tokenId = tokenId,
                callback = CallbackHandle(
                    onSuccess = {authResult.invoke(it)},
                    onError = {}
                )
            )
        }

    }
}