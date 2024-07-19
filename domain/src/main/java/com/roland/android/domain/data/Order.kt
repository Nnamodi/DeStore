package com.roland.android.domain.data

import java.util.Date
import kotlin.collections.List

data class Order(
	val id: Int = 0,
	val orderNo: String = "",
	val dateOfPurchase: Date = Date(),
	val amount: Float = 0f
)

data class OrderDetails(
	val orderNo: String = "",
	val orderItems: List<CartItem> = emptyList(),
	val dateOfPurchase: Date = Date(),
	val amount: Float = 0f
)
