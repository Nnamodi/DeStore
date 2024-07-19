package com.roland.android.destore.ui.screen.cart

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.destore.data.cartItemsFlow
import com.roland.android.destore.utils.Extensions.filterCartItems
import com.roland.android.domain.data.CartItem
import com.roland.android.domain.data.State
import com.roland.android.domain.usecase.CartUseCase
import com.roland.android.domain.usecase.CartUseCaseActions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

class CartViewModel : ViewModel(), KoinComponent {
	private val cartUseCase: CartUseCase by inject()

	private val _cartUiState = MutableStateFlow(CartUiState())
	var cartUiState by mutableStateOf(_cartUiState.value); private set

	private var cartItems by mutableStateOf<List<CartItem>>(emptyList())

	init {
		fetchCartItems()

		viewModelScope.launch {
			cartItemsFlow.collect { items ->
				cartItems = items
				_cartUiState.update { it.copy(cartItems = cartItems) }
				Log.i("LocalData", "From cart: $items")
			}
		}
		viewModelScope.launch {
			_cartUiState.collect {
				cartUiState = it
			}
		}
	}

	private fun fetchCartItems() {
		viewModelScope.launch {
			cartUseCase.execute(
				CartUseCase.Request(
					CartUseCaseActions.GetCartItems
				)
			)
				.collect { data ->
					if (data !is State.Success) return@collect
					cartItemsFlow.value = data.data.cartItems
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
		val itemsInCart = cartItems.filterCartItems(item)
		val itemToAdd = itemsInCart[0].apply {
			generatedId = UUID.randomUUID().hashCode()
		}
		viewModelScope.launch {
			cartUseCase.execute(
				CartUseCase.Request(
					CartUseCaseActions.AddToCart(itemToAdd)
				)
			)
				.collect { data ->
					if (data !is State.Success) return@collect
					cartItemsFlow.value = data.data.cartItems
				}
		}
	}

	private fun remove(item: CartItem) {
		val itemsInCart = cartItems.filterCartItems(item)
		Log.i("LocalData", "Remove: $itemsInCart\n$cartItems")
		viewModelScope.launch {
			cartUseCase.execute(
				CartUseCase.Request(
					CartUseCaseActions.RemoveFromCart(itemsInCart.last())
				)
			)
				.collect { data ->
					if (data !is State.Success) return@collect
					cartItemsFlow.value = data.data.cartItems
				}
		}
	}

	private fun removeFromCart(item: CartItem) {
		val itemsInCart = cartItems.filterCartItems(item)
		viewModelScope.launch {
			cartUseCase.execute(
				CartUseCase.Request(
					CartUseCaseActions.RemoveItemsFromCart(itemsInCart)
				)
			)
				.collect { data ->
					if (data !is State.Success) return@collect
					cartItemsFlow.value = data.data.cartItems
				}
		}
	}

}