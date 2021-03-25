package com.pierre.yugiohkotlinapp.recycler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pierre.yugiohkotlinapp.room.CardApplication
import com.pierre.yugiohkotlinapp.R
import com.pierre.yugiohkotlinapp.ScanActivity
import com.pierre.yugiohkotlinapp.room.CardViewModel
import com.pierre.yugiohkotlinapp.room.CardViewModelFactory

class CardsListActivity : AppCompatActivity() {

	private val cardViewModel: CardViewModel by viewModels {
		CardViewModelFactory((application as CardApplication).repository)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_cards_list)

		val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
		val adapter = CardListAdapter()
		recyclerView.adapter = adapter
		recyclerView.layoutManager = LinearLayoutManager(this)

		// Add an observer on the LiveData returned by getAlphabetizedWords.
		// The onChanged() method fires when the observed data changes and the activity is
		// in the foreground.
		cardViewModel.allCards.observe(this) { card ->
			// Update the cached copy of the cards in the adapter.
			card.let { adapter.submitList(it) }
		}

		val fab = findViewById<FloatingActionButton>(R.id.fab)
		fab.setOnClickListener {
			val intent = Intent(this, ScanActivity::class.java)
			startActivity(intent)
		}
	}
}
