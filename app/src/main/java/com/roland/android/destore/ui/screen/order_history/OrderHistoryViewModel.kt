package com.roland.android.destore.ui.screen.order_history

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.destore.data.ordersFlow
import com.roland.android.domain.data.Order
import com.roland.android.domain.data.OrderDetails
import com.roland.android.domain.data.State
import com.roland.android.domain.usecase.OrdersUseCase
import com.roland.android.domain.usecase.OrdersUseCaseActions
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OrderHistoryViewModel : ViewModel(), KoinComponent {
	private val ordersUseCase: OrdersUseCase by inject()

	var orders by mutableStateOf<List<Order>>(emptyList()); private set
	var orderDetails by mutableStateOf(OrderDetails()); private set

	init {
		fetchOrdersHistory()

		viewModelScope.launch {
			ordersFlow.collect { items ->
				orders = items
			}
		}
	}

	private fun fetchOrdersHistory() {
		viewModelScope.launch {
			ordersUseCase.execute(
				OrdersUseCase.Request(
					OrdersUseCaseActions.GetOrderHistory
				)
			)
				.collect { data ->
					Log.i("LocalData", "Order history: $data")
					if (data !is State.Success) return@collect
					ordersFlow.value = data.data.orders
				}
		}
	}

	fun fetchOrderDetails(orderNo: String) {
		viewModelScope.launch {
			ordersUseCase.execute(
				OrdersUseCase.Request(
					OrdersUseCaseActions.GetOrder(orderNo)
				)
			)
				.collect { data ->
					if (data !is State.Success) return@collect
					orderDetails = data.data.orderDetails
				}
		}
	}

}