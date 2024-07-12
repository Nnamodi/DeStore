package com.roland.android.destore.ui.screen.details

import com.roland.android.remotedatasource.usecase.data.Item

sealed class DetailsActions {

	data class AddToCart(val item: Item) : DetailsActions()

	data class Favorite(
		val item: Item,
		val favorite: Boolean,
	) : DetailsActions()

	data object Reload : DetailsActions()

}