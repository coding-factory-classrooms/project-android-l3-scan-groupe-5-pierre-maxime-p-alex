package com.pierre.yugiohkotlinapp.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pierre.yugiohkotlinapp.R
import com.pierre.yugiohkotlinapp.room.CardEntity

class CardListAdapter : ListAdapter<CardEntity, CardListAdapter.CardViewHolder>(WordsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.cardName)
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(text: String?) {
            wordItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): CardViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return CardViewHolder(view)
            }
        }
    }

    class WordsComparator : DiffUtil.ItemCallback<CardEntity>() {
        override fun areItemsTheSame(oldItem: CardEntity, newItem: CardEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CardEntity, newItem: CardEntity): Boolean {
            return oldItem.cardId == newItem.cardId
        }
    }
}