package com.roland.android.remotedatasource.network.model

import com.squareup.moshi.Json

internal data class ImageModel(
	@Json(name = "url")
	val url: String = ""
)
