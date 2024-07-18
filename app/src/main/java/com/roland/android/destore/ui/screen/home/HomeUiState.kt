package com.roland.android.destore.ui.screen.home

import com.roland.android.domain.data.Item
import com.roland.android.domain.data.State

data class HomeUiState(
	val data: State<HomeDataModel>? = null,
	val userInfo: String = "Nnamdi Igede",
	val favoriteItems: List<Item> = emptyList(),
)

data class HomeDataModel(
	val featured: List<Item>,
	val specialOffers: List<Item>
)
