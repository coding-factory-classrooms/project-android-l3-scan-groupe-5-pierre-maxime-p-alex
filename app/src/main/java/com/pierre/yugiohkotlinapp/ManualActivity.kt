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
            // Update the cached copy of the words in the adapter.
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
            if (cardsData?.cardsData != null) {
                val cardData = CardEntity(
                    cardsData.cardsData.first().id,
                    cardsData.cardsData.first().name,
                    cardsData.cardsData.first().desc,
                    cardsData.cardsData.first().id,
                    cardsData.cardsData.first().cardImages.first().imageUrl,
                    cardsData.cardsData.first().atk,
                    cardsData.cardsData.first().def,
                    cardsData.cardsData.first().level,
                    cardsData.cardsData.first().race,
                    "${cardsData.cardsData.first().cardPrices.first().ebayPrice} $"
                )
                
                showAlert(
                    "Carte ajoutée",
                    applicationContext,
                    this
                )
                val intent = Intent(this, CardsListActivity::class.java)
                startActivity(intent)
            } else {
                showAlert(
                    "Carte introuvable",
                    applicationContext,
                    this
                )
            }
        })
    }
}