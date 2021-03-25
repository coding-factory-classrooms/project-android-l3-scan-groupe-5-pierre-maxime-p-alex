package com.pierre.yugiohkotlinapp.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class RetrofitInstance {
	companion object {
		private const val BASE_URL = "https://db.ygoprodeck.com/api/v7/"

		private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
			this.level = HttpLoggingInterceptor.Level.BODY
		}
		private val client: OkHttpClient = OkHttpClient.Builder().apply {
			this.addInterceptor(interceptor)
				.connectTimeout(30, TimeUnit.SECONDS)
				.readTimeout(20, TimeUnit.SECONDS)
				.writeTimeout(25, TimeUnit.SECONDS)
		}.build()

		fun getRetrofitInstance(): Retrofit {
			return Retrofit.Builder()
				.baseUrl(BASE_URL)
				.client(client)
				.addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
				.build()
		}
	}
}
