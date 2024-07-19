package com.roland.android.domain.usecase

import android.util.Log
import com.roland.android.domain.data.Order
import com.roland.android.domain.data.OrderDetails
import com.roland.android.domain.repository.local.OrdersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OrdersUseCase : UseCase<OrdersUseCase.Request, OrdersUseCase.Response>(), KoinComponent {

	private val ordersRepository: OrdersRepository by inject()

	override fun process(request: Request): Flow<Response> {
		return when (request.action) {
			OrdersUseCaseActions.GetOrderHistory -> {
				ordersRepository.getOrderHistory().map {
					Log.i("LocalData", "Order fetched: $it")
					Response(it)
				}
			}
			is OrdersUseCaseActions.GetOrder -> {
				ordersRepository.getOrder(request.action.orderNo).map {
					Log.i("LocalData", "Order Details: $it")
					Response(orderDetails = it)
				}
			}
			is OrdersUseCaseActions.SaveOrders -> {
				ordersRepository.saveOrder(request.action.order).map { Response(it) }
			}
		}
	}

	data class Request(val action: OrdersUseCaseActions) : UseCase.Request

	data class Response(
		val orders: List<Order> = emptyList(),
		val orderDetails: OrderDetails = OrderDetails()
	) : UseCase.Response

}

sealed class OrdersUseCaseActions {

	data object GetOrderHistory : OrdersUseCaseActions()

	data class GetOrder(val orderNo: String) : OrdersUseCaseActions()

	data class SaveOrders(val order: OrderDetails) : OrdersUseCaseActions()

}