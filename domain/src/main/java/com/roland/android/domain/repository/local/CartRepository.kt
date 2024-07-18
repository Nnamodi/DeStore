package com.roland.android.domain.repository.local

import com.roland.android.domain.data.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {

	fun getCartItems(): Flow<List<CartItem>>

	fun addToCart(cartItem: CartItem): Flow<List<CartItem>>

	fun removeFromCart(cartItem: CartItem): Flow<List<CartItem>>

	fun removeItemsFromCart(cartItems: List<CartItem>): Flow<List<CartItem>>

}