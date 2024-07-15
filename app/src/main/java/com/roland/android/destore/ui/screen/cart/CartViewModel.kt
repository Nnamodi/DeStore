package com.roland.android.destore.ui.screen.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.destore.data.cartItemsFlow
import com.roland.android.destore.ui.screen.checkout.CheckoutUiState
import com.roland.android.remotedatasource.usecase.data.CartItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class CartViewModel : ViewModel(), KoinComponent {

	private val _cartUiState = MutableStateFlow(CartUiState())
	var cartUiState by mutableStateOf(_cartUiState.value)

	private val _checkoutUiState = MutableStateFlow(CheckoutUiState())
	var checkoutUiState by mutableStateOf(_checkoutUiState.value)

	private var cartItems by mutableStateOf<List<CartItem>>(emptyList())

	init {
		viewModelScope.launch {
			cartItemsFlow.collect { items ->
				cartItems = items
				_cartUiState.update { it.copy(cartItems = cartItems) }
				_checkoutUiState.update { it.copy(cartItems = cartItems) }
			}
		}
		viewModelScope.launch {
			_cartUiState.collect {
				cartUiState = it
			}
		}
		viewModelScope.launch {
			_checkoutUiState.collect {
				checkoutUiState = it
			}
		}
	}

	fun actions(action: CartActions) {
		when (action) {
			is CartActions.Add -> add(action.item)
			is CartActions.Remove -> remove(action.item)
			is CartActions.RemoveFromCart -> removeFromCart(action.item)
			CartActions.Checkout -> checkout()
		}
	}

	private fun add(item: CartItem) {
		val itemInCart = cartItems.filter { it.id == item.id }
		cartItemsFlow.value = cartItems + itemInCart[0]
	}

	private fun remove(item: CartItem) {
		val itemsInCart = cartItems.filter { it.id == item.id }
		cartItemsFlow.value = cartItems - itemsInCart.last()
	}

	private fun removeFromCart(item: CartItem) {
		val itemsInCart = cartItems.filter { it.id == item.id }
		itemsInCart.forEach {
			cartItemsFlow.value = cartItems - it
		}
	}

	private fun checkout() {
		viewModelScope.launch {
			delay(700)
			cartItemsFlow.value = emptyList()
		}
	}

}