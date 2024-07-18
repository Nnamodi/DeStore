package com.roland.android.destore.ui.screen.details

import com.roland.android.domain.data.Item
import com.roland.android.domain.data.ItemDetails
import com.roland.android.domain.data.State

data class DetailsUiState(
	val details: State<ItemDetails>? = null,
	val moreInStore: State<List<Item>>? = null,
	val category: String = "",
	val favorited: Boolean = false,
	val numberInCart: Int = 0,
	val favoriteItems: List<Item> = emptyList()
)
