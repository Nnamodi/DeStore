package com.roland.android.destore.ui.screen.checkout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.destore.data.cartItemsFlow
import com.roland.android.destore.data.ordersFlow
import com.roland.android.destore.utils.Extensions.round
import com.roland.android.domain.data.CartItem
import com.roland.android.domain.data.OrderDetails
import com.roland.android.domain.data.State
import com.roland.android.domain.usecase.CartUseCase
import com.roland.android.domain.usecase.CartUseCaseActions
import com.roland.android.domain.usecase.OrdersUseCase
import com.roland.android.domain.usecase.OrdersUseCaseActions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar
import java.util.UUID

class CheckoutViewModel : ViewModel(), KoinComponent {
	private val cartUseCase: CartUseCase by inject()
	private val ordersUseCase: OrdersUseCase by inject()

	private val _checkoutUiState = MutableStateFlow(CheckoutUiState())
	var checkoutUiState by mutableStateOf(_checkoutUiState.value); private set

	private var cartItems by mutableStateOf<List<CartItem>>(emptyList())

	init {
		viewModelScope.launch {
			cartItemsFlow.collect { items ->
				cartItems = items
				_checkoutUiState.update { it.copy(cartItems = cartItems) }
			}
		}
		viewModelScope.launch {
			_checkoutUiState.collect {
				checkoutUiState = it
			}
		}
	}

	fun actions(action: CheckoutActions) {
		when (action) {
			CheckoutActions.Checkout -> checkout()
		}
	}

	private fun checkout() {
		viewModelScope.launch {
			val order = OrderDetails(
				orderNo = UUID.randomUUID().toString().takeLast(12),
				orderItems = cartItems,
				dateOfPurchase = Calendar.getInstance().time,
				amount = calculateAmount()
			)
			ordersUseCase.execute(
				OrdersUseCase.Request(
					OrdersUseCaseActions.SaveOrders(order)
				)
			)
				.collect { data ->
					if (data !is State.Success) return@collect
					ordersFlow.value = data.data.orders
					clearCart()
				}
		}
	}

	private fun clearCart() {
		viewModelScope.launch {
			cartUseCase.execute(
				CartUseCase.Request(
					CartUseCaseActions.RemoveItemsFromCart(cartItems)
				)
			)
				.collect { data ->
					if (data !is State.Success) return@collect
					cartItemsFlow.value = data.data.cartItems
				}
		}
	}

	private fun calculateAmount(): Float {
		val subTotal = cartItems.sumOf { it.currentPrice.toDouble() }.round()
		val deliveryFee = ((5 / 100.0) * subTotal).round()
		val discount = ((3 / 100.0) * subTotal).round()
		val total = (subTotal + deliveryFee - discount).toDouble().round()
		return total
	}

}