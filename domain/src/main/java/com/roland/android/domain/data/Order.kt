package com.roland.android.domain.data

import java.util.Date
import kotlin.collections.List

data class Order(
	val id: String = "",
	val orders: List<CartItem> = emptyList(),
	val dateOfPurchase: Date = Date()
)
