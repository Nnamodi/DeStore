package com.roland.android.localdatasource.repository

import com.roland.android.domain.data.Order
import com.roland.android.domain.repository.local.OrdersRepository
import com.roland.android.localdatasource.database.OrdersDao
import com.roland.android.localdatasource.entity.OrderEntity
import com.roland.android.localdatasource.utils.Converters.convertFromOrders
import com.roland.android.localdatasource.utils.Converters.convertToOrders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
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

	override fun getOrder(orderId: Int): Flow<Order> {
		return orderDao.getOrder(orderId).map {
			convertToOrders(it)
		}
	}

	override fun saveOrder(order: Order): Flow<List<Order>> {
		coroutineScope.launch {
			val item = convertFromOrders(order)
			orderDao.saveOrder(item)
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