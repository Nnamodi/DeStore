package com.roland.android.domain.repository.local

import com.roland.android.domain.data.Order
import com.roland.android.domain.data.OrderDetails
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {

	fun getOrderHistory(): Flow<List<Order>>

	fun getOrder(orderNo: String): Flow<OrderDetails>

	fun saveOrder(orderDetails: OrderDetails): Flow<List<Order>>

}