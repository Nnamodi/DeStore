package com.roland.android.destore.ui.screen.list

import com.roland.android.domain.data.Item

sealed class ListActions {

	data class AddToCart(
		val item: Item,
		val color: Long,
		val size: Int
	) : ListActions()

	data class Favorite(
		val item: Item,
		val favorite: Boolean,
	) : ListActions()

	data object Reload : ListActions()

}