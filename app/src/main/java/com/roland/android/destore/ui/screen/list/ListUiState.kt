package com.roland.android.destore.ui.screen.list

import com.roland.android.domain.data.Item
import com.roland.android.domain.data.State

data class ListUiState(
	val allItems: State<List<Item>>? = null,
	val categorizedItems: State<List<Item>>? = null,
	val wishlistItems: List<Item> = emptyList()
)
