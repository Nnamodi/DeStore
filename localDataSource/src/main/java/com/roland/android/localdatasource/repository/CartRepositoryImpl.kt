package com.roland.android.localdatasource.repository

import com.roland.android.domain.data.CartItem
import com.roland.android.domain.repository.local.CartRepository
import com.roland.android.localdatasource.database.CartDao
import com.roland.android.localdatasource.entity.CartItemEntity
import com.roland.android.localdatasource.utils.Converters.convertFromCartItem
import com.roland.android.localdatasource.utils.Converters.convertToCartItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CartRepositoryImpl : CartRepository, KoinComponent {

	private val cartDao: CartDao by inject()
	private val coroutineScope: CoroutineScope by inject()

	override fun getCartItems(): Flow<List<CartItem>> {
		return cartDao.getCartItems().mapCartItems()
	}

	override fun addToCart(cartItem: CartItem): Flow<List<CartItem>> {
		coroutineScope.launch {
			val item = convertFromCartItem(cartItem)
			cartDao.addToCart(item)
		}
		return cartDao.getCartItems().mapCartItems()
	}

	override fun removeFromCart(cartItem: CartItem): Flow<List<CartItem>> {
		coroutineScope.launch {
			val item = convertFromCartItem(cartItem)
			cartDao.removeFromCart(item)
		}
		return cartDao.getCartItems().mapCartItems()
	}

	override fun removeItemsFromCart(cartItems: List<CartItem>): Flow<List<CartItem>> {
		coroutineScope.launch {
			val items = cartItems.map { convertFromCartItem(it) }
			items.forEach {
				cartDao.removeFromCart(it)
			}
		}
		return cartDao.getCartItems().mapCartItems()
	}

	private fun Flow<List<CartItemEntity>>.mapCartItems(): Flow<List<CartItem>> {
		return map { cartItemEntities ->
			cartItemEntities.map {
				convertToCartItem(it)
			}
		}
	}

}