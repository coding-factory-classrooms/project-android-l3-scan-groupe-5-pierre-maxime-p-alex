package com.pierre.yugiohkotlinapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.pierre.yugiohkotlinapp.databinding.ActivityDetailsBinding
import com.squareup.picasso.Picasso

class DetailsActivity : AppCompatActivity() {

	private lateinit var binding: ActivityDetailsBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDetailsBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val cardName: String = intent.getStringExtra("card_name").toString()
		val cardLevel: Int = intent.getIntExtra("card_level", 0)
		val cardAtk: Int = intent.getIntExtra("card_atk", 0)
		val cardDef: Int = intent.getIntExtra("card_def", 0)
		val cardRace: String = intent.getStringExtra("card_race").toString()
		val cardDesc: String = intent.getStringExtra("card_desc").toString()
		val cardPrice: String = intent.getStringExtra("card_price").toString()
		val cardImage: String = intent.getStringExtra("card_image").toString()
		val cardId: Int = intent.getIntExtra("card_id", 0)

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
}
