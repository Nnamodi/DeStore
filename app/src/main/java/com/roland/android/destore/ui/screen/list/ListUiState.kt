package com.roland.android.destore.ui.screen.list

import com.roland.android.domain.data.Item
import com.roland.android.domain.data.State

data class ListUiState(
	val data: State<List<Item>>? = null,
	val favoriteItems: List<Item> = emptyList()
)
