package com.pierre.yugiohkotlinapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.pierre.yugiohkotlinapp.api.RetrofitInstance
import com.pierre.yugiohkotlinapp.cards.CardService
import com.pierre.yugiohkotlinapp.cards.ProDeckModels
import com.pierre.yugiohkotlinapp.recycler.CardListAdapter
import com.pierre.yugiohkotlinapp.recycler.CardsListActivity
import com.pierre.yugiohkotlinapp.room.CardApplication
import com.pierre.yugiohkotlinapp.room.CardEntity
import com.pierre.yugiohkotlinapp.room.CardViewModel
import com.pierre.yugiohkotlinapp.room.CardViewModelFactory
import com.pierre.yugiohkotlinapp.utils.Utils.showAlert
import kotlinx.android.synthetic.main.activity_manual.*
import retrofit2.Response

class ManualActivity : AppCompatActivity() {

	private val cardViewModel: CardViewModel by viewModels {
		CardViewModelFactory((application as CardApplication).repository)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_manual)
		val adapter = CardListAdapter()

		cardViewModel.allCards.observe(this) { card ->
			// Update the cached copy of the cards in the adapter.
			card.let { adapter.submitList(it) }
		}
	}

	fun searchCardById(view: View) {

		val cardId = editCardId.text.toString().toInt()

		val retrofitService = RetrofitInstance
			.getRetrofitInstance()
			.create(CardService::class.java)

		val responseLiveData: LiveData<Response<ProDeckModels>> = liveData {
			val response = retrofitService.getCardById(cardId)
			emit(response)
		}

		responseLiveData.observe(this, Observer {
			val cardsData = it.body()
			if (cardsData?.cardsData == null) {
				showAlert(
					"Carte introuvable",
					applicationContext,
					this
				)
				return@Observer
			}

			val cardBody = cardsData.cardsData.first()

			val cardData = CardEntity(
				0,
				cardBody.name,
				cardBody.desc,
				cardBody.id,
				cardBody.cardImages.first().imageUrl,
				cardBody.atk,
				cardBody.def,
				cardBody.level,
				cardBody.race,
				"${cardBody.cardPrices.first().ebayPrice} $"
			)

			val cardDatabase = cardViewModel.allCards.value

			if (cardDatabase != null) {
				val isExist = cardDatabase.any{ it.cardId == cardBody.id}
				if(isExist) {
					showAlert(
						"Carte déjà sauvegardée",
						applicationContext,
						this
					)
					return@Observer
				}
				Thread(Runnable {
					cardViewModel.insert(cardData)
				}).start()
			}

			showAlert(
				"Carte ajoutée",
				applicationContext,
				this
			)
			val intent = Intent(this, CardsListActivity::class.java)
			startActivity(intent)
		})
	}
}
