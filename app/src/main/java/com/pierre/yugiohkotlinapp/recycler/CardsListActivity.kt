package com.pierre.yugiohkotlinapp.recycler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pierre.yugiohkotlinapp.DetailsActivity
import com.pierre.yugiohkotlinapp.room.CardApplication
import com.pierre.yugiohkotlinapp.ScanActivity
import com.pierre.yugiohkotlinapp.databinding.ActivityCardsListBinding
import com.pierre.yugiohkotlinapp.room.CardViewModel
import com.pierre.yugiohkotlinapp.room.CardViewModelFactory

class CardsListActivity : AppCompatActivity() {

	private val cardViewModel: CardViewModel by viewModels {
		CardViewModelFactory((application as CardApplication).repository)
	}

	private lateinit var binding: ActivityCardsListBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityCardsListBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val recyclerView = binding.recyclerview
		val adapter = CardListAdapter()
		recyclerView.adapter = adapter
		recyclerView.layoutManager = LinearLayoutManager(this)

		adapter.onItemClick = { card ->
			val intent = Intent(this, DetailsActivity::class.java)
			intent.putExtra("card_name", card.cardName)
			intent.putExtra("card_level", card.cardLevel)
			intent.putExtra("card_atk", card.cardAtk)
			intent.putExtra("card_def", card.cardDef)
			intent.putExtra("card_race", card.cardRace)
			intent.putExtra("card_desc", card.cardDesc)
			intent.putExtra("card_price", card.cardPrice)
			intent.putExtra("card_image", card.imageUrl)
			intent.putExtra("card_id", card.cardId)
			startActivity(intent)
		}

		// Add an observer on the LiveData returned by getAlphabetizedWords.
		// The onChanged() method fires when the observed data changes and the activity is
		// in the foreground.
		cardViewModel.allCards.observe(this) { card ->
			// Update the cached copy of the cards in the adapter.
			card.let { adapter.submitList(it) }
		}

		val fab = binding.fab
		fab.setOnClickListener {
			val intent = Intent(this, ScanActivity::class.java)
			startActivity(intent)
		}
	}
}
