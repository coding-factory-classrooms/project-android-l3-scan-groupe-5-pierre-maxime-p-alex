package com.pierre.yugiohkotlinapp.cards

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CardService {
	@GET("cardinfo.php?&id=89631139")
	suspend fun getCard(): Response<ProDeckModels>

	@GET("cardinfo.php")
	suspend fun getCardById(@Query("id") id: Int): Response<ProDeckModels>

	@GET("cardinfo.php")
	suspend fun getCardByString(@Query("id") id: String): Response<ProDeckModels>
}
