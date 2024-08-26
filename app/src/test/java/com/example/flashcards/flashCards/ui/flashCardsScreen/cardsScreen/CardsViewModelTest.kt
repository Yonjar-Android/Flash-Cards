@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.flashcards.flashCards.ui.flashCardsScreen.cardsScreen

import app.cash.turbine.test
import com.example.flashcards.TestCoroutineRule
import com.example.flashcards.flashCards.data.repositories.FlashCardsRepository
import com.example.flashcards.motherObject.FlashCardMotherObject
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CardsViewModelTest{
    @get:Rule
    val testCoroutine = TestCoroutineRule()

    @MockK
    private lateinit var repository: FlashCardsRepository

    lateinit var viewModel: CardsViewModel

    val error = "An error has occurred"

    val flashCard = FlashCardMotherObject.flashCard

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        viewModel = CardsViewModel(repository)
    }

    @Test
    fun `updateFlashCard should emit Success state if ResultFlashCard_Success is returned`() = runTest {

        //Given
        coEvery { repository.updateFlashCard(flashCard) } returns FlashCardMotherObject.resultFlashCardSuccessCreationMsg
        coEvery { repository.getFlashCards() } returns FlashCardMotherObject.resultFlashCardSuccess

        //When
        viewModel.updateFlashCard(flashCard)

        //Then
        viewModel.state.test {
            assertTrue(awaitItem() is CardsScreenState.Loading)
            advanceUntilIdle()
            val state = awaitItem()
            assertTrue(state is CardsScreenState.Success)
            assertEquals((state as CardsScreenState.Success).message, FlashCardMotherObject.resultFlashCardSuccessCreationMsg.data)
            advanceUntilIdle()
            assertEquals((awaitItem() as CardsScreenState.Success).message, "")
        }

    }
}