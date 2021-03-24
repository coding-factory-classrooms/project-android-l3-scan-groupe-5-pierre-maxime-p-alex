package com.pierre.yugiohkotlinapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.pierre.yugiohkotlinapp.api.RetrofitInstance
import com.pierre.yugiohkotlinapp.cards.CardService
import com.pierre.yugiohkotlinapp.cards.ProDeckModels
import com.pierre.yugiohkotlinapp.utils.Utils.showAlert
import kotlinx.android.synthetic.main.activity_cards_list.*
import kotlinx.android.synthetic.main.activity_manual.*
import retrofit2.Response

class ManualActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual)
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
                val result = " " + "Card ID : ${cardsData.cardsData.first().id}" + "\n" +
                        " " + "Card Name : ${cardsData.cardsData.first().name}" + "\n" +
                        " " + "Card Image : ${cardsData.cardsData.first().cardImages.first().imageUrl}" + "\n" +
                        " " + "Card Atk : ${cardsData.cardsData.first().atk}" + "\n" +
                        " " + "Card Def : ${cardsData.cardsData.first().def}" + "\n\n\n"
                Log.i("CARDETAILS", result)
                showAlert(
                    "Carte ajoutée",
                    applicationContext,
                    this
                )

            } else {
                showAlert(
                    "Carte introuvable",
                    applicationContext,
                    this
                )

            }
        })

        // Return une alert si l'ID correspond à aucune carte
        // Sinon rediriger vers la liste des cartes en y ajoutant du coup la nouvelle qui viens d'être saisi
    }
}