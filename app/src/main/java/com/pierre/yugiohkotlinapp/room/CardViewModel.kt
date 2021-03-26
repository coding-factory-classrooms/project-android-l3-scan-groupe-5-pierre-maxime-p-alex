package com.pierre.yugiohkotlinapp.room

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class CardViewModel(private val repository: CardRepository) : ViewModel() {

	// Using LiveData and caching what allCards returns has several benefits:
	// - We can put an observer on the data (instead of polling for changes) and only update the
	//   the UI when the data actually changes.
	// - Repository is completely separated from the UI through the ViewModel.
	val allCards: LiveData<List<CardEntity>> = repository.allCards.asLiveData()

	/**
	 * Launching a new coroutine to insert the data in a non-blocking way
	 */
	fun insert(card: CardEntity) = viewModelScope.launch {
		repository.addCard(card)
	}

	fun deleteCardById(cardId: Int) = viewModelScope.launch {
		repository.deleteCardById(cardId)
	}
}

class CardViewModelFactory(private val repository: CardRepository) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
			@Suppress("UNCHECKED_CAST")
			return CardViewModel(repository) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}
