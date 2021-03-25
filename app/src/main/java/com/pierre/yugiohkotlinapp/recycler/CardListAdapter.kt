package com.pierre.yugiohkotlinapp.recycler

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pierre.yugiohkotlinapp.R
import com.pierre.yugiohkotlinapp.room.CardEntity
import com.squareup.picasso.Picasso

class CardListAdapter : ListAdapter<CardEntity, CardListAdapter.CardViewHolder>(CardsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.cardName, current.imageUrl.toUri(), current.cardLevel)
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		//Remplacer par des Bindings pour ne pas faire findViewById
        private val cardName: TextView = itemView.findViewById(R.id.cardNametextView)
		private val cardImage: ImageView = itemView.findViewById(R.id.cardImageView)
		private val cardRare: TextView = itemView.findViewById(R.id.cardRaretextView)


		fun bind(name: String?, imageUrl: Uri?, rare: Int? ) {
			cardName.text = name
			Picasso.get().load(imageUrl).into(cardImage);
			cardRare.text = rare.toString()
        }

        companion object {
            fun create(parent: ViewGroup): CardViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return CardViewHolder(view)
            }
        }
    }

    class CardsComparator : DiffUtil.ItemCallback<CardEntity>() {
        override fun areItemsTheSame(oldItem: CardEntity, newItem: CardEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CardEntity, newItem: CardEntity): Boolean {
            return oldItem.cardId == newItem.cardId
        }
    }
}
