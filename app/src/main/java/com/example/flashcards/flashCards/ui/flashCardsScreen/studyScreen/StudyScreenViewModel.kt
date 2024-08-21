package com.example.flashcards.flashCards.ui.flashCardsScreen.studyScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.flashCards.data.repositories.FlashCardsRepository
import com.example.flashcards.flashCards.data.repositories.ResultFlashCard
import com.example.flashcards.flashCards.domain.models.FlashCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyScreenViewModel @Inject constructor(private val repository: FlashCardsRepository) :
    ViewModel() {

    private val _state = MutableStateFlow<StudyScreenState>(StudyScreenState.Initial)
    val state: StateFlow<StudyScreenState> = _state

    private val _flashCards = MutableStateFlow<List<FlashCard>>(listOf())
    val flashcards: StateFlow<List<FlashCard>> = _flashCards

    fun getFlashCards() {
        viewModelScope.launch {
            try {
                val response = repository.getFlashCards()

                when (response) {
                    is ResultFlashCard.Error -> {
                        _state.value = StudyScreenState.Error(response.error)
                    }

                    is ResultFlashCard.Success -> {
                        if (response.data.isEmpty()) {
                            _state.value = StudyScreenState.Error(error = "No hay cartas para estudiar")
                            _flashCards.value = mutableListOf()
                        } else {
                            _state.value = StudyScreenState.Success()
                            _flashCards.value = response.data
                        }
                    }
                }
            } catch (e: Exception) {
                _state.value = StudyScreenState.Error(e.message ?: "")
            }
        }
    }

    fun createFlashCard(title: String, answer: String) {

        _state.value = StudyScreenState.Loading

        if (!validations(title, answer)) return

        viewModelScope.launch {
            try {
                val response = repository.createFlashCard(title, answer)

                when (response) {
                    is ResultFlashCard.Error -> {
                        _state.value = StudyScreenState.Error(response.error)
                    }

                    is ResultFlashCard.Success -> {
                        _state.value = StudyScreenState.Success(message = response.data)
                        getFlashCards()
                    }
                }
            } catch (e: Exception) {
                _state.value = StudyScreenState.Error(e.message ?: "")
            }
        }
    }

    private fun validations(title:String, answer: String):Boolean{
        return if (title.isBlank() || answer.isBlank()){
            _state.value = StudyScreenState.Error("No puede dejar ninguno de los campos en blanco")
            false
        } else{
            true
        }

    }

    fun updateFlashCardReview(flashCard: FlashCard) {
        viewModelScope.launch {
            try {
                val response = repository.updateFlashCardNextReview(flashCard)

                when (response) {
                    is ResultFlashCard.Error -> {
                        _state.value = StudyScreenState.Error(response.error)

                    }

                    is ResultFlashCard.Success -> {
                        _state.value = StudyScreenState.Success()
                        getFlashCards()
                    }
                }

            } catch (e: Exception) {
                _state.value = StudyScreenState.Error(e.message ?: "")
            }
        }
    }

    fun resetState() {
        _state.value = StudyScreenState.Initial
    }

}