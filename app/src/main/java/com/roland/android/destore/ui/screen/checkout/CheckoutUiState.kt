package com.roland.android.destore.ui.screen.checkout

import com.roland.android.remotedatasource.usecase.data.CartItem
import com.roland.android.destore.data.UserInfo

data class CheckoutUiState(
	val cartItems: List<CartItem> = emptyList(),
	val userInfo: UserInfo = UserInfo()
)
