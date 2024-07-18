package com.roland.android.domain.data

import kotlin.collections.List

data class ItemDetails(
	val id: String = "",
	val name: String = "",
	val description: String = "",
	val photos: List<Image> = emptyList(),
	val currentPrice: String? = null,
	val categories: List<Category> = emptyList()
)
