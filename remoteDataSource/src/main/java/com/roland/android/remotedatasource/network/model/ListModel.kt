package com.roland.android.remotedatasource.network.model

import com.squareup.moshi.Json

internal data class ListModel(
	@Json(name = "items")
	val items: List<ItemModel> = emptyList(),
	@Json(name = "size")
	val size: Int = 20
)
