package com.roland.android.destore.ui.screen.details

import com.roland.android.remotedatasource.usecase.data.Item
import com.roland.android.remotedatasource.usecase.data.ItemDetails
import com.roland.android.remotedatasource.utils.State

data class DetailsUiState(
	val data: State<DetailsDataModel>? = null,
	val favorited: Boolean = false,
	val numberInCart: Int = 0
)

data class DetailsDataModel(
	val details: ItemDetails,
	val moreInStore: List<Item>
)
