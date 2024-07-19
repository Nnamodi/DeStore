package com.roland.android.localdatasource.repository

import com.roland.android.domain.data.Order
import com.roland.android.domain.data.OrderDetails
import com.roland.android.domain.repository.local.OrdersRepository
import com.roland.android.localdatasource.database.OrdersDao
import com.roland.android.localdatasource.entity.OrderEntity
import com.roland.android.localdatasource.utils.Converters.convertFromOrderDetails
import com.roland.android.localdatasource.utils.Converters.convertToOrderDetails
import com.roland.android.localdatasource.utils.Converters.convertToOrderItem
import com.roland.android.localdatasource.utils.Converters.convertToOrders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OrdersRepositoryImpl : OrdersRepository, KoinComponent {

	private val orderDao: OrdersDao by inject()
	private val coroutineScope: CoroutineScope by inject()

	override fun getOrderHistory(): Flow<List<Order>> {
		return orderDao.getOrderHistory().mapOrders()
	}

	override fun getOrder(orderNo: String): Flow<OrderDetails> = combine(
		orderDao.getOrder(orderNo),
		orderDao.getOrderItems(orderNo)
	) { order, orderItems ->
		convertToOrderDetails(order, orderItems)
	}

	override fun saveOrder(orderDetails: OrderDetails): Flow<List<Order>> {
		coroutineScope.launch {
			val item = convertFromOrderDetails(orderDetails)
			orderDao.saveOrder(item)

			val orderItems = orderDetails.orderItems
				.map { convertToOrderItem(it) }
				.onEach { it.orderNo = orderDetails.orderNo }
			orderDao.saveOrderItems(orderItems)
		}
		return orderDao.getOrderHistory().mapOrders()
	}

	private fun Flow<List<OrderEntity>>.mapOrders(): Flow<List<Order>> {
		return map { orderEntities ->
			orderEntities.map {
				convertToOrders(it)
			}
		}
	}

}