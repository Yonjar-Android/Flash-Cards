package com.example.flashcards.flashCards.data.repositories

import com.example.flashcards.flashCards.utils.CallbackHandle
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class AuthRepositoryImpTest{

    @MockK
    lateinit var firebaseAuth: FirebaseAuth

    @MockK
    lateinit var authCredential: AuthCredential

    private lateinit var repositoryImp: AuthRepositoryImp

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        mockkStatic(GoogleAuthProvider::class)
        every { GoogleAuthProvider.getCredential(any(), null) } returns authCredential
        repositoryImp = AuthRepositoryImp(firebaseAuth)
    }

    @Test
    fun `signInWithGoogle should invoke onSuccess when authentication is successful`()  = runBlocking {
        val mockCallback = mockk<CallbackHandle<Boolean>>(relaxed = true)
        val mockTask = mockk<Task<AuthResult>>(relaxed = true)

        every { firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(any()) } answers {
            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(mockTask)
            mockTask
        }

        every { mockTask.isSuccessful } returns true

        repositoryImp.signInWithGoogle("tokenId", mockCallback)

        verify { mockCallback.onSuccess(true) }
    }

    @Test
    fun `signInWithGoogle should invoke onError when an exception occurs`() = runBlocking {
        val mockCallback = mockk<CallbackHandle<Boolean>>(relaxed = true)

        every { firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(any()) } throws Exception()

        repositoryImp.signInWithGoogle("tokenId", mockCallback)

        verify { mockCallback.onError(null) }
    }

}