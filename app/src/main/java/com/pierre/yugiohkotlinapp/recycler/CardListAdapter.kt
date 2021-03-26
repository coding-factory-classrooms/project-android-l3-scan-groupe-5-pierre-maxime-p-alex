package com.pierre.yugiohkotlinapp.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pierre.yugiohkotlinapp.databinding.RecyclerviewItemBinding
import com.pierre.yugiohkotlinapp.room.CardEntity
import com.squareup.picasso.Picasso

class CardListAdapter : ListAdapter<CardEntity, CardListAdapter.CardViewHolder>(CardsComparator()) {

	var onItemClick: ((CardEntity) -> Unit)? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = RecyclerviewItemBinding.inflate(inflater, parent, false)
		return CardViewHolder(binding)
	}

	override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
		val current = getItem(position)
		with(holder) {
			cardName.text = current.cardName
			Picasso.get().load(current.imageUrl).into(cardImage);
			cardRare.text = current.cardLevel.toString()
			itemView.setOnClickListener {
				onItemClick?.invoke(current)
			}
		}
	}

	class CardViewHolder(private val binding: RecyclerviewItemBinding) :
		RecyclerView.ViewHolder(binding.root) {
		val cardName: TextView = binding.cardNametextView
		val cardImage: ImageView = binding.cardImageView
		val cardRare: TextView = binding.cardRaretextView
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
