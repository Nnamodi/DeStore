package com.roland.android.localdatasource

import com.roland.android.domain.data.CartItem
import com.roland.android.domain.data.Order
import com.roland.android.domain.data.OrderDetails
import com.roland.android.localdatasource.database.OrdersDao
import com.roland.android.localdatasource.entity.OrderEntity
import com.roland.android.localdatasource.entity.OrderItemEntity
import com.roland.android.localdatasource.repository.OrdersRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date

class OrdersRepositoryImplTest {

	private val ordersDao = mock<OrdersDao>()
	private val ordersRepository = mock<OrdersRepositoryImpl>()

	@Test
	fun testGetOrderHistory() = runTest {
		val orderNo = ""
		val localOrders = listOf(OrderEntity(0, orderNo, Date(), 0f))
		whenever(ordersDao.getOrderHistory()).thenReturn(flowOf(localOrders))

		val expectedOrders = listOf(Order(0, orderNo, Date(), 0f))
		val savedOrders = ordersRepository.getOrderHistory().first()
		assertEquals(expectedOrders, savedOrders)
	}

	@Test
	fun testGetOrderDetails() = runTest {
		val orderNo = ""
		val localOrder = OrderEntity(0, orderNo, Date(), 0f)
		val localOrderItems = listOf(OrderItemEntity(id  = "", photoUrl = "", name = "", currentPrice = "", color = 0, size = 0))

		whenever(ordersDao.getOrder(orderNo)).thenReturn(flowOf(localOrder))
		whenever(ordersDao.getOrderItems(orderNo)).thenReturn(flowOf(localOrderItems))

		val expectedOrder = OrderDetails(orderNo, listOf(CartItem()), Date(), 0f)
		val savedOrder = ordersRepository.getOrder(orderNo).first()
		assertEquals(expectedOrder, savedOrder)
	}

	@Test
	fun testSaveOrder() = runTest {
		val orderNo = ""
		val savedOrder = OrderDetails(orderNo, listOf(CartItem()), Date(), 0f)
		ordersRepository.saveOrder(savedOrder)

		val newOrders = OrderEntity(0, orderNo, Date(), 0f)
		val newOrderItems = listOf(OrderItemEntity(id  = "", photoUrl = "", name = "", currentPrice = "", color = 0, size = 0))
		verify(ordersDao).saveOrder(newOrders)
		verify(ordersDao).saveOrderItems(newOrderItems)
	}

}