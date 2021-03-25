package com.pierre.yugiohkotlinapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM cards")
    fun getAllCard(): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE cardId IN (:cardId)")
    fun getCardById(cardId: Int): List<CardEntity>

    @Query("SELECT * FROM cards WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): CardEntity

    @Query("DELETE FROM cards")
    suspend fun deleteAll()

    @Insert
    fun addCard(vararg card: CardEntity)

    @Delete
    fun delete(cardId: CardEntity)
}