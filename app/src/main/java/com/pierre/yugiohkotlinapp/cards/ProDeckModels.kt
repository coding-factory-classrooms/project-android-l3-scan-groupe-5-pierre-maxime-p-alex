package com.pierre.yugiohkotlinapp.cards


import com.google.gson.annotations.SerializedName

data class ProDeckModels(
	@SerializedName("data")
	val cardsData: List<Data>
)

data class Data(
	@SerializedName("atk")
	val atk: Int,
	@SerializedName("attribute")
	val attribute: String,
	@SerializedName("card_images")
	val cardImages: List<CardImage>,
	@SerializedName("card_prices")
	val cardPrices: List<CardPrice>,
	@SerializedName("def")
	val def: Int,
	@SerializedName("desc")
	val desc: String,
	@SerializedName("id")
	val id: Int,
	@SerializedName("level")
	val level: Int,
	@SerializedName("name")
	val name: String,
	@SerializedName("race")
	val race: String,
	@SerializedName("type")
	val type: String
)

data class CardPrice(
	@SerializedName("amazon_price")
	val amazonPrice: String,
	@SerializedName("cardmarket_price")
	val cardmarketPrice: String,
	@SerializedName("coolstuffinc_price")
	val coolstuffincPrice: String,
	@SerializedName("ebay_price")
	val ebayPrice: String,
	@SerializedName("tcgplayer_price")
	val tcgplayerPrice: String
)

data class CardImage(
	@SerializedName("id")
	val id: Int,
	@SerializedName("image_url")
	val imageUrl: String,
	@SerializedName("image_url_small")
	val imageUrlSmall: String
)

