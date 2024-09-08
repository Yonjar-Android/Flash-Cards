package com.example.flashcards.flashCards.ui.initialScreen

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.flashCards.data.repositories.AuthRepositoryImp
import com.example.flashcards.flashCards.utils.CallbackHandle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitialScreenViewModel @Inject constructor(
    private val authRepositoryImp: AuthRepositoryImp,
    private val context: Application
) :
    ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    var isLoggedIn by mutableStateOf(false)
        private set

    init {
        checkIfUserIsLoggedIn()
    }

    private fun checkIfUserIsLoggedIn() {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            // Si hay una cuenta iniciada, notifica que la sesión sigue activa
            isLoggedIn = true
        } else {
            // Si no hay sesión iniciada
            isLoggedIn = false
        }
        _isLoading.value = false
    }

    fun loginWithGoogleAccount(tokenId: String, authResult: (Boolean) -> Unit) {

        viewModelScope.launch {
            authRepositoryImp.signInWithGoogle(tokenId = tokenId,
                callback = CallbackHandle(
                    onSuccess = { authResult.invoke(it) },
                    onError = {}
                )
            )
        }

    }
}