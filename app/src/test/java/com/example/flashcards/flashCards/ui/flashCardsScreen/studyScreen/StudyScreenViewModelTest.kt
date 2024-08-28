package com.example.flashcards.flashCards.ui.flashCardsScreen.studyScreen

import app.cash.turbine.test
import com.example.flashcards.TestCoroutineRule
import com.example.flashcards.flashCards.data.repositories.FlashCardsRepository
import com.example.flashcards.flashCards.domain.models.FlashCard
import com.example.flashcards.flashCards.utils.ResourceProvider
import com.example.flashcards.motherObject.FlashCardMotherObject
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*


@OptIn(ExperimentalCoroutinesApi::class)
class StudyScreenViewModelTest {

    @get:Rule
    val testCoroutine = TestCoroutineRule()

    @MockK
    lateinit var repository: FlashCardsRepository

    @MockK
    lateinit var resourceProvider: ResourceProvider

    private lateinit var viewModel: StudyScreenViewModel

    private val errorMessage = "An error has occurred"
    private val successMessage = "Success Message"

    private val title: String = "Apple"

    private val answer: String = "Manzana"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = StudyScreenViewModel(repository,resourceProvider)
    }

    @Test
    fun `getFlashCards should emit success state if the list received from repository is not empty`() =
        runTest {

            //Given

            coEvery { repository.getFlashCards() } returns FlashCardMotherObject.resultFlashCardSuccess

            //When

            viewModel.getFlashCards()

            //Then


            viewModel.state.test {
                assertTrue(awaitItem() is StudyScreenState.Initial)
                advanceUntilIdle()
                assertTrue(awaitItem() is StudyScreenState.Success)
                assertEquals(viewModel.flashcards.value, FlashCardMotherObject.flashCardsList)
            }
        }

    @Test
    fun `getFlashCards should emit error state if ResultFlashCards_Error is received`() = runTest {
        //Given

        coEvery { repository.getFlashCards() } returns FlashCardMotherObject.resultFlashCardError

        //When

        viewModel.getFlashCards()

        //Then


        viewModel.state.test {
            assertTrue(awaitItem() is StudyScreenState.Initial)
            advanceUntilIdle()
            val state = awaitItem()
            assertTrue(state is StudyScreenState.Error)
            val errorState = state as StudyScreenState.Error
            assertEquals(errorState.error, FlashCardMotherObject.resultFlashCardError.error)
        }
    }

    @Test
    fun `getFlashCards should emit error state if an Exception occurs`() = runTest {
        //Given

        coEvery { repository.getFlashCards() } throws RuntimeException(errorMessage)

        //When

        viewModel.getFlashCards()

        //Then


        viewModel.state.test {
            assertTrue(awaitItem() is StudyScreenState.Initial)
            advanceUntilIdle()
            val state = awaitItem()
            assertTrue(state is StudyScreenState.Error)
            val errorState = state as StudyScreenState.Error
            assertEquals(errorState.error, errorMessage)
        }
    }

    @Test
    fun `createFlashCard should emit success state if ResultFlashCard_Success is received`() =
        runTest {

            //Given
            coEvery {
                repository.createFlashCard(
                    title,
                    answer
                )
            } returns FlashCardMotherObject.resultFlashCardSuccessCreationMsg
            coEvery { repository.getFlashCards() } returns FlashCardMotherObject.resultFlashCardSuccess // Simula la respuesta para getFlashCards()

            //When
            viewModel.createFlashCard(title, answer)

            //Then
            viewModel.state.test {
                assertTrue(awaitItem() is StudyScreenState.Loading)
                advanceUntilIdle()

                val state = awaitItem()
                assertTrue(state is StudyScreenState.Success)
                val stateSuccess = state as StudyScreenState.Success
                assertEquals(stateSuccess.message, successMessage)
                // The first SuccessState finish here
                advanceUntilIdle()
                assertEquals((awaitItem() as StudyScreenState.Success).message, "")
                // The second Success state is here
            }
        }


    @Test
    fun `createFlashCard should emit error state if ResultFlashCard_Error is received`() = runTest {

        //Given
        coEvery {
            repository.createFlashCard(
                title,
                answer
            )
        } returns FlashCardMotherObject.resultFlashCardError

        //When
        viewModel.createFlashCard(title, answer)

        //Then
        viewModel.state.test {
            assertTrue(awaitItem() is StudyScreenState.Loading)
            advanceUntilIdle()

            val state = awaitItem()
            assertTrue(state is StudyScreenState.Error)
            val errorState = state as StudyScreenState.Error
            assertEquals(errorState.error, errorMessage)
        }
    }

    @Test
    fun `createFlashCard should emit error state if an Exception occurs`() = runTest {

        //Given
        coEvery { repository.createFlashCard(title, answer) } throws RuntimeException(errorMessage)

        //When
        viewModel.createFlashCard(title, answer)

        //Then
        viewModel.state.test {
            assertTrue(awaitItem() is StudyScreenState.Loading)
            advanceUntilIdle()

            val state = awaitItem()
            assertTrue(state is StudyScreenState.Error)
            val errorState = state as StudyScreenState.Error
            assertEquals(errorState.error, errorMessage)
        }
    }

    @Test
    fun `updateFlashCardReview should emit success state if ResultFlashCard_Success is received`() =
        runTest {

            //Given
            coEvery { repository.updateFlashCardNextReview(FlashCardMotherObject.flashCard) } returns FlashCardMotherObject.resultFlashCardSuccessCreationMsg
            coEvery { repository.getFlashCards() } returns FlashCardMotherObject.resultFlashCardSuccess // Simula la respuesta para getFlashCards()

            //When
            viewModel.updateFlashCardReview(FlashCardMotherObject.flashCard)

            //Then
            viewModel.state.test {
                assertTrue(awaitItem() is StudyScreenState.Initial)
                advanceUntilIdle()

                // The first SuccessState finish here
                val state = awaitItem()
                assertTrue(state is StudyScreenState.Success)
                val stateSuccess = state as StudyScreenState.Success
                assertEquals(stateSuccess.message, "")

            }
        }

    @Test
    fun `updateFlashCardReview should emit error state if ResultFlashCard_Error is received`() =
        runTest {

            //Given
            coEvery { repository.updateFlashCardNextReview(FlashCardMotherObject.flashCard) } returns FlashCardMotherObject.resultFlashCardError

            //When
            viewModel.updateFlashCardReview(FlashCardMotherObject.flashCard)

            //Then
            viewModel.state.test {
                assertTrue(awaitItem() is StudyScreenState.Initial)
                advanceUntilIdle()

                val state = awaitItem()
                assertTrue(state is StudyScreenState.Error)
                val errorState = state as StudyScreenState.Error
                assertEquals(errorState.error, errorMessage)
            }
        }

    @Test
    fun `updateFlashCardReview should emit error state if ResultFlashCard_Error if an Exception occurs`() =
        runTest {

            //Given
            coEvery { repository.updateFlashCardNextReview(FlashCardMotherObject.flashCard) } throws RuntimeException(
                errorMessage
            )

            //When
            viewModel.updateFlashCardReview(FlashCardMotherObject.flashCard)

            //Then
            viewModel.state.test {
                assertTrue(awaitItem() is StudyScreenState.Initial)
                advanceUntilIdle()

                val state = awaitItem()
                assertTrue(state is StudyScreenState.Error)
                val errorState = state as StudyScreenState.Error
                assertEquals(errorState.error, errorMessage)
            }
        }
}
