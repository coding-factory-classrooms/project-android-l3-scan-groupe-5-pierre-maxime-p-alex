package com.pierre.yugiohkotlinapp.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pierre.yugiohkotlinapp.R
import com.pierre.yugiohkotlinapp.room.CardEntity
import com.squareup.picasso.Picasso



class CardListAdapter : ListAdapter<CardEntity, CardListAdapter.CardViewHolder> (CardsComparator()) {


	var onItemClick: ((CardEntity) -> Unit)? = null


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
		val view: View = LayoutInflater.from(parent.context)
			.inflate(R.layout.recyclerview_item, parent, false)
		return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val current = getItem(position)
		with(holder){
			cardName.text = current.cardName
			Picasso.get().load(current.imageUrl).into(cardImage);
			cardRare.text = current.cardLevel.toString()
			itemView.setOnClickListener {
				onItemClick?.invoke(current)
			}
		}
    }



	class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		//Remplacer par des Bindings pour ne pas faire findViewById
         val cardName: TextView = itemView.findViewById(R.id.cardNametextView)
		 val cardImage: ImageView = itemView.findViewById(R.id.cardImageView)
		 val cardRare: TextView = itemView.findViewById(R.id.cardRaretextView)
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
