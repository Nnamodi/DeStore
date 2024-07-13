package com.roland.android.destore.ui.screen.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.destore.data.cartItemsFlow
import com.roland.android.destore.data.favoriteItemsFlow
import com.roland.android.destore.ui.screen.checkout.CheckoutUiState
import com.roland.android.destore.utils.Extensions.toCartItem
import com.roland.android.destore.utils.ResponseConverter.convertToCategoryData
import com.roland.android.remotedatasource.usecase.GetCategoryUseCase
import com.roland.android.remotedatasource.usecase.GetProductUseCase
import com.roland.android.remotedatasource.usecase.data.CartItem
import com.roland.android.remotedatasource.usecase.data.Item
import com.roland.android.remotedatasource.utils.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CartViewModel : ViewModel(), KoinComponent {
	private val getProductUseCase: GetProductUseCase by inject()
	private val getCategoryUseCase: GetCategoryUseCase by inject()

	private val _cartUiState = MutableStateFlow(CartUiState())
	var cartUiState by mutableStateOf(_cartUiState.value)

	private val _checkoutUiState = MutableStateFlow(CheckoutUiState())
	var checkoutUiState by mutableStateOf(_checkoutUiState.value)

	var cartItems by mutableStateOf<List<CartItem>>(emptyList())
	var productId by mutableStateOf("")

	init {
		viewModelScope.launch {
			cartItemsFlow.collect {
				cartItems = it
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
		}
	}

	private fun add(item: CartItem) {
		val itemInCart = cartItems.filter { it.id == item.id }
		cartItemsFlow.value = cartItems + itemInCart[0]
	}

	private fun remove(item: CartItem) {
		val itemsInCart = cartItems.filter { it.id == item.id }
		cartItemsFlow.value = cartItems - itemsInCart.random()
	}

	private fun removeFromCart(item: CartItem) {
		val itemsInCart = cartItems.filter { it.id == item.id }
		cartItemsFlow.value = cartItems - itemsInCart.random()
	}

}