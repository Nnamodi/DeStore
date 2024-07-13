package com.roland.android.destore.ui.screen.cart

import com.roland.android.remotedatasource.usecase.data.CartItem

sealed class CartActions {

	data class Add(val item: CartItem) : CartActions()

	data class Remove(val item: CartItem) : CartActions()

	data class RemoveFromCart(val item: CartItem) : CartActions()

}