package com.pierre.yugiohkotlinapp.recycler

import com.pierre.yugiohkotlinapp.room.CardEntity

interface OnCardClickListener {
    fun onCardClick(item: CardEntity)
}