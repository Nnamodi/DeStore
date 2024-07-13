package com.roland.android.remotedatasource.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class CategoryModel(
	@Json(name = "id")
	val id: String = "",
	@Json(name = "name")
	val name: String = ""
)
