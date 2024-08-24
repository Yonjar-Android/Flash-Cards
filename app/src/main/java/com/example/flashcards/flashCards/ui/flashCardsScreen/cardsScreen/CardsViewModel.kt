package com.example.flashcards.flashCards.ui.flashCardsScreen.cardsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.flashCards.data.repositories.FlashCardsRepository
import com.example.flashcards.flashCards.data.repositories.ResultFlashCard
import com.example.flashcards.flashCards.domain.models.FlashCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val repository: FlashCardsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<CardsScreenState>(CardsScreenState.Loading)
    val state: StateFlow<CardsScreenState> = _state

    private val _flashCards = MutableStateFlow<List<FlashCard>>(listOf())
    val flashCards:StateFlow<List<FlashCard>> = _flashCards

    fun getFlashCards() {
        viewModelScope.launch {
            try {
                val response = repository.getFlashCards()

                when(response){
                    is ResultFlashCard.Error -> {
                        _state.update { CardsScreenState.Error(response.error) }
                    }
                    is ResultFlashCard.Success -> {
                        _state.update { CardsScreenState.Success("") }
                        _flashCards.update { response.data }
                    }
                }

            } catch (e: Exception) {
                _state.update { CardsScreenState.Error(e.message ?: "") }
            }
        }
    }

    fun deleteFlashCard(flashCardId:String){

        _state.value = CardsScreenState.Loading

        viewModelScope.launch {
            try {

                val response = repository.deleteFlashCard(flashCardId)

                when(response){
                    is ResultFlashCard.Error -> {
                        _state.update { CardsScreenState.Error(response.error) }
                    }
                    is ResultFlashCard.Success -> {
                        _state.update { CardsScreenState.Success(message = response.data) }
                        getFlashCards()
                    }
                }

            } catch (e:Exception){
                _state.update { CardsScreenState.Error(e.message ?: "") }
            }
        }
    }

    fun resetState() {
        _state.update { CardsScreenState.Initial }
    }

}