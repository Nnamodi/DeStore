package com.roland.android.domain.data

import kotlin.collections.List

data class Item(
	val id: String = "",
	val name: String = "",
	val photos: List<Image> = emptyList(),
	val currentPrice: String = "",
	val category: Category = Category()
) {

	fun isFavorite(favorites: List<Item>): Boolean {
		return favorites.any { it.id == id }
	}

}

data class CartItem(
	var generatedId: Int = 0,
	val id: String = "",
	val name: String = "",
	val photo: Image = Image(),
	val currentPrice: String = "",
	val color: Long = 0,
	var size: Int = 0
)
