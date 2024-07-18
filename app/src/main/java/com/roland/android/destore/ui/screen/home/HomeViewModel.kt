package com.roland.android.destore.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.destore.data.cartItemsFlow
import com.roland.android.destore.data.favoriteItemsFlow
import com.roland.android.destore.ui.components.Colors
import com.roland.android.destore.ui.screen.details.Sizes
import com.roland.android.destore.utils.Extensions.toCartItem
import com.roland.android.destore.utils.ResponseConverter.convertToHomeData
import com.roland.android.domain.usecase.GetProductsUseCase
import com.roland.android.domain.data.CartItem
import com.roland.android.domain.data.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel : ViewModel(), KoinComponent {
	private val getProductsUseCase: GetProductsUseCase by inject()

	private val _homeUiState = MutableStateFlow(HomeUiState())
	var homeUiState by mutableStateOf(_homeUiState.value)
	private var cartItems by mutableStateOf<List<CartItem>>(emptyList())
	var favoriteItems by mutableStateOf<List<Item>>(emptyList())

	init {
		fetchData()

		viewModelScope.launch {
			cartItemsFlow.collect {
				cartItems = it
			}
		}
		viewModelScope.launch {
			favoriteItemsFlow.collect { items ->
				favoriteItems = items
				_homeUiState.update { it.copy(favoriteItems = favoriteItems) }
			}
		}
		viewModelScope.launch {
			_homeUiState.collect {
				homeUiState = it
			}
		}
	}

	private fun fetchData() {
		viewModelScope.launch {
			getProductsUseCase.execute(GetProductsUseCase.Request)
				.map { convertToHomeData(it) }
				.collect { data ->
					_homeUiState.update { it.copy(data = data) }
				}
		}
	}

	fun actions(action: HomeActions) {
		when (action) {
			is HomeActions.AddToCart -> addToCart(action.item)
			is HomeActions.Favorite -> favorite(action.item, action.favorite)
			HomeActions.Reload -> reload()
		}
	}

	private fun addToCart(item: Item) {
		val color = Colors.entries.random().color.value.toLong()
		val size = Sizes.entries.random().value
		cartItemsFlow.value = cartItems + item.toCartItem(color, size)
	}

	private fun favorite(
		item: Item,
		favorite: Boolean
	) {
		favoriteItemsFlow.value = if (favorite) {
			favoriteItems - item
		} else { favoriteItems + item }
	}

	private fun reload() {
		_homeUiState.update { it.copy(data = null) }
		fetchData()
	}

}