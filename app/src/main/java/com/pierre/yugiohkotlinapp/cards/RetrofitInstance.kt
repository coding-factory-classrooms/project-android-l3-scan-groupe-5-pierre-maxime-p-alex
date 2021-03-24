package com.pierre.yugiohkotlinapp.cards

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient

class RetrofitInstance {
	companion object {
		private const val BASE_URL = "https://db.ygoprodeck.com/api/v7/"

		private val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
			this.level = HttpLoggingInterceptor.Level.BODY
		}
		private val client : OkHttpClient = OkHttpClient.Builder().apply {
			this.addInterceptor(interceptor)
		}.build()

		fun getRetrofitInstance(): Retrofit {
			return Retrofit.Builder()
				.baseUrl(BASE_URL)
				.addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
				.client(client)
				.build()
		}
	}
}
