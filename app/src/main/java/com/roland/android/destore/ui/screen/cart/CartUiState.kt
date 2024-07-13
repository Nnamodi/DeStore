package com.roland.android.destore.ui.screen.cart

import com.roland.android.remotedatasource.usecase.data.CartItem
import com.roland.android.remotedatasource.usecase.data.Item
import com.roland.android.remotedatasource.utils.State

data class CartUiState(
	val cartItems: List<CartItem> = emptyList()
)
