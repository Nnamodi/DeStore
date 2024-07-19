package com.roland.android.localdatasource.utils

import com.roland.android.domain.data.CartItem
import com.roland.android.domain.data.Image
import com.roland.android.domain.data.Order
import com.roland.android.domain.data.OrderDetails
import com.roland.android.localdatasource.entity.CartItemEntity
import com.roland.android.localdatasource.entity.OrderEntity
import com.roland.android.localdatasource.entity.OrderItemEntity
import com.roland.android.localdatasource.entity.WishlistItemEntity

internal object Converters {

	fun convertToCartItem(cartItemEntity: CartItemEntity) = CartItem(
		generatedId = cartItemEntity.generatedId,
		id = cartItemEntity.id,
		name = cartItemEntity.name,
		photo = Image(cartItemEntity.photoUrl),
		currentPrice = cartItemEntity.currentPrice,
		color = cartItemEntity.color,
		size = cartItemEntity.size
	)

	fun convertFromCartItem(cartItem: CartItem) = CartItemEntity(
		generatedId = cartItem.generatedId,
		id = cartItem.id,
		name = cartItem.name,
		photoUrl = cartItem.photo.url,
		currentPrice = cartItem.currentPrice,
		color = cartItem.color,
		size = cartItem.size
	)

	fun convertToOrders(orderEntity: OrderEntity) = Order(
		orderNo = orderEntity.orderNo,
		dateOfPurchase = orderEntity.dateOfPurchase,
		amount = orderEntity.amount
	)

	fun convertToOrderDetails(
		orderEntity: OrderEntity,
		orderItems: List<OrderItemEntity>
	) = OrderDetails(
		orderNo = orderEntity.orderNo,
		orderItems = orderItems.map { convertFromOrderItem(it) },
		dateOfPurchase = orderEntity.dateOfPurchase,
		amount = orderEntity.amount
	)

	fun convertFromOrderDetails(orderDetails: OrderDetails) = OrderEntity(
		orderNo = orderDetails.orderNo,
		dateOfPurchase = orderDetails.dateOfPurchase,
		amount = orderDetails.amount
	)

	fun convertToWishlist(wishlistItemEntity: WishlistItemEntity) = wishlistItemEntity.itemId

	fun convertFromWishlist(itemId: String) = WishlistItemEntity(itemId = itemId)

	fun convertToOrderItem(cartItem: CartItem) = OrderItemEntity(
		id = cartItem.id,
		name = cartItem.name,
		photoUrl = cartItem.photo.url,
		currentPrice = cartItem.currentPrice,
		color = cartItem.color,
		size = cartItem.size
	)

	private fun convertFromOrderItem(orderItemEntity: OrderItemEntity) = CartItem(
		id = orderItemEntity.id,
		name = orderItemEntity.name,
		photo = Image(orderItemEntity.photoUrl),
		currentPrice = orderItemEntity.currentPrice,
		color = orderItemEntity.color,
		size = orderItemEntity.size
	)

}