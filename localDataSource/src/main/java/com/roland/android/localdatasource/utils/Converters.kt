package com.roland.android.localdatasource.utils

import com.roland.android.domain.data.CartItem
import com.roland.android.domain.data.Image
import com.roland.android.domain.data.Order
import com.roland.android.localdatasource.entity.CartItemEntity
import com.roland.android.localdatasource.entity.OrderEntity
import com.roland.android.localdatasource.entity.WishlistItemEntity

internal object Converters {

	fun convertToCartItem(cartItemEntity: CartItemEntity) = CartItem(
		id = cartItemEntity.id,
		name = cartItemEntity.name,
		photo = Image(cartItemEntity.photoUrl),
		currentPrice = cartItemEntity.currentPrice,
		color = cartItemEntity.color,
		size = cartItemEntity.size
	)

	fun convertFromCartItem(cartItem: CartItem) = CartItemEntity(
		id = cartItem.id,
		name = cartItem.name,
		photoUrl = cartItem.photo.url,
		currentPrice = cartItem.currentPrice,
		color = cartItem.color,
		size = cartItem.size
	)

	fun convertToOrders(orderEntity: OrderEntity) = Order(
		id = orderEntity.id,
		orders = orderEntity.convertToCartItemsList(),
		dateOfPurchase = orderEntity.dateOfPurchase
	)

	fun convertFromOrders(order: Order) = OrderEntity(
		id = order.id,
		orderIds = order.orders.map { it.id },
		orderNames = order.orders.map { it.name },
		orderPhotosUrl = order.orders.map { it.photo.url },
		orderPrices = order.orders.map { it.currentPrice },
		orderColors = order.orders.map { it.color },
		orderSizes = order.orders.map { it.size },
		dateOfPurchase = order.dateOfPurchase
	)

	fun convertToWishlist(wishlistItemEntity: WishlistItemEntity) = wishlistItemEntity.itemId

	fun convertFromWishlist(itemId: String) = WishlistItemEntity(
		itemId = itemId
	)

	private fun OrderEntity.convertToCartItemsList(): List<CartItem> {
		return orderIds.mapIndexed { index, id ->
			CartItem(
				id = id,
				name = orderNames[index],
				photo = Image(orderPhotosUrl[index]),
				currentPrice = orderPrices[index],
				color = orderColors[index],
				size = orderSizes[index],
			)
		}
	}

}