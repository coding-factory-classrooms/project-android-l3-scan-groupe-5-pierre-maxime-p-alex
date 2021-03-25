package com.pierre.yugiohkotlinapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardEntity(
	@PrimaryKey(autoGenerate = true) val uid: Int,
	@ColumnInfo(name = "name") val cardName: String,
	@ColumnInfo(name = "desc") val cardDesc: String,
	@ColumnInfo(name = "cardId") val cardId: Int,
	@ColumnInfo(name = "imageUrl") val imageUrl: String,
	@ColumnInfo(name = "atk") val cardAtk: Int,
	@ColumnInfo(name = "def") val cardDef: Int,
	@ColumnInfo(name = "level") val cardLevel: Int,
	@ColumnInfo(name = "race") val cardRace: String,
	@ColumnInfo(name = "price") val cardPrice: String,
)
