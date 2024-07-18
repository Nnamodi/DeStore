package com.roland.android.domain.repository.local

import com.roland.android.domain.data.Order
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {

	fun getOrderHistory(): Flow<List<Order>>

	fun getOrder(orderId: Int): Flow<Order>

	fun saveOrder(order: Order): Flow<List<Order>>

}