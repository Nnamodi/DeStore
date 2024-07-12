package com.roland.android.remotedatasource.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ItemModel(
	@Json(name = "id")
	val id: String = "",
	@Json(name = "name")
	val name: String = "",
	@Json(name = "description")
	val price: String = "", // a workaround concerning the bug I narrated in `PriceModel` file
	@Json(name = "photos")
	val photos: List<ImageModel> = emptyList(),
	@Json(name = "current_price")
	val currentPrice: List<PriceModel> = emptyList()
)
