package com.roland.android.destore.ui.screen.cart

import com.roland.android.remotedatasource.usecase.data.CartItem

data class CartUiState(
	val cartItems: List<CartItem> = emptyList()
)
