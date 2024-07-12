package com.roland.android.destore.ui.screen.home

import com.roland.android.remotedatasource.usecase.data.Item

sealed class HomeActions {

	data class AddToCart(val item: Item) : HomeActions()

	data class Favorite(
		val item: Item,
		val favorite: Boolean,
	) : HomeActions()

	data object Reload : HomeActions()

}