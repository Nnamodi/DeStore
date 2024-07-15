package com.roland.android.remotedatasource.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlin.collections.List

@JsonClass(generateAdapter = true)
internal data class ItemDetailsModel(
	@Json(name = "id")
	val id: String = "",
	@Json(name = "name")
	val name: String = "",
	@Json(name = "description")
	val description: String = "",
	@Json(name = "photos")
	val photos: List<ImageModel> = emptyList(),
	@Json(name = "current_price")
	val currentPrice: String? = null,
	@Json(name = "categories")
	val categories: List<CategoryModel> = emptyList()
)
