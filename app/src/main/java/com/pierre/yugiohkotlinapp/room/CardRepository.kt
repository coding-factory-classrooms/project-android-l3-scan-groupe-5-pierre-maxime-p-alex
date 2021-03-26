package com.pierre.yugiohkotlinapp.room

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class CardRepository(private val cardDao: CardDao) {

	// Room executes all queries on a separate thread.
	// Observed Flow will notify the observer when the data has changed.
	val allCards: Flow<List<CardEntity>> = cardDao.getAllCard()

	// By default Room runs suspend queries off the main thread, therefore, we don't need to
	// implement anything else to ensure we're not doing long running database work
	// off the main thread.
	@Suppress("addCard")
	@WorkerThread
	suspend fun addCard(card: CardEntity) {
		cardDao.addCard(card)
	}

	@Suppress("deleteCardById")
	@WorkerThread
	suspend fun deleteCardById(cardId: Int) {
		cardDao.deleteCardById(cardId)
	}
}
