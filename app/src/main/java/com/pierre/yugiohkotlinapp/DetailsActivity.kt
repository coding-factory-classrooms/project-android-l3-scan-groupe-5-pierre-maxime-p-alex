package com.pierre.yugiohkotlinapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.pierre.yugiohkotlinapp.databinding.ActivityDetailsBinding
import com.pierre.yugiohkotlinapp.recycler.CardListAdapter
import com.pierre.yugiohkotlinapp.recycler.CardsListActivity
import com.pierre.yugiohkotlinapp.room.CardApplication
import com.pierre.yugiohkotlinapp.room.CardViewModel
import com.pierre.yugiohkotlinapp.room.CardViewModelFactory
import com.pierre.yugiohkotlinapp.utils.Utils
import com.squareup.picasso.Picasso

class DetailsActivity : AppCompatActivity() {

	private val cardViewModel: CardViewModel by viewModels {
		CardViewModelFactory((application as CardApplication).repository)
	}
	private lateinit var binding: ActivityDetailsBinding


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDetailsBinding.inflate(layoutInflater)
		setContentView(binding.root)
		val adapter = CardListAdapter()

		val cardName: String = intent.getStringExtra("card_name").toString()
		val cardLevel: Int = intent.getIntExtra("card_level", 0)
		val cardAtk: Int = intent.getIntExtra("card_atk", 0)
		val cardDef: Int = intent.getIntExtra("card_def", 0)
		val cardRace: String = intent.getStringExtra("card_race").toString()
		val cardDesc: String = intent.getStringExtra("card_desc").toString()
		val cardPrice: String = intent.getStringExtra("card_price").toString()
		val cardImage: String = intent.getStringExtra("card_image").toString()
		val cardId: Int = intent.getIntExtra("card_id", 0)

		cardViewModel.allCards.observe(this) { card ->
			// Update the cached copy of the cards in the adapter.
			card.let { adapter.submitList(it) }
		}

		binding.atkTextView.text = cardAtk.toString()
		binding.cardIdTextView.text = cardId.toString()
		binding.defTextView.text = cardDef.toString()
		binding.descTextView.text = cardDesc
		Picasso.get().load(cardImage).into(binding.cardImageView);
		binding.levelTextView.text = cardLevel.toString()
		binding.nameTextView.text = cardName
		binding.priceTextView.text = cardPrice
		binding.raceTextView.text = cardRace
	}

	fun deleteCard(view: View) {
		cardViewModel.deleteCardById(binding.cardIdTextView.text.toString().toInt())
		val intent = Intent(this, CardsListActivity::class.java)
		startActivity(intent)
		Utils.showAlert(
			"Carte supprimée",
			applicationContext,
			this
		)
	}
}
