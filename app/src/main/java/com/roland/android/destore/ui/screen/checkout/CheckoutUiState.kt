package com.roland.android.destore.ui.screen.checkout

import com.roland.android.destore.data.UserInfo
import com.roland.android.remotedatasource.usecase.data.CartItem

data class CheckoutUiState(
	val cartItems: List<CartItem> = emptyList(),
	val userInfo: UserInfo = UserInfo()
)
