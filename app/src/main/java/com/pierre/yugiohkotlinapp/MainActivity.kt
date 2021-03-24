package com.pierre.yugiohkotlinapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.pierre.yugiohkotlinapp.cards.CardService
import com.pierre.yugiohkotlinapp.cards.ProDeckModels
import com.pierre.yugiohkotlinapp.cards.RetrofitInstance
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
		val retrofitService = RetrofitInstance
			.getRetrofitInstance()
			.create(CardService::class.java)

		val responseLiveData:LiveData<Response<ProDeckModels>> = liveData {
			val response = retrofitService.getCardById(89631139)
			emit(response)
		}
		responseLiveData.observe(this, Observer {
			val cardsData = it.body()
			if(cardsData != null){
				val result = " "+ "Card ID : ${cardsData.cardsData.first().id}"+"\n"+
						" "+ "Card Name : ${cardsData.cardsData.first().name}"+"\n"+
						" "+ "Card Image : ${cardsData.cardsData.first().cardImages.first().imageUrl}"+"\n"+
						" "+ "Card Atk : ${cardsData.cardsData.first().atk}"+"\n"+
						" "+ "Card Def : ${cardsData.cardsData.first().def}"+"\n\n\n"
				Log.i("CARDACTIVITY", result)
				textView.append(result)
			}
		})
    }
}
