package com.roland.android.remotedatasource.usecase.data

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
	val id: String = "",
	val name: String = "",
	val photos: List<Image> = emptyList(),
	val currentPrice: String = "",
	val color: Int = 0,
	var size: Int = 0,
	var numberInCart: Int = 0
)
