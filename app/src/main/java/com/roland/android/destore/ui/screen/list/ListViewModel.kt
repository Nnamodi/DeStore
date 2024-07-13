package com.roland.android.destore.ui.screen.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.destore.data.cartItemsFlow
import com.roland.android.destore.data.favoriteItemsFlow
import com.roland.android.destore.utils.Extensions.toCartItem
import com.roland.android.destore.utils.ResponseConverter.convertToCategoryData
import com.roland.android.remotedatasource.usecase.GetCategoryUseCase
import com.roland.android.remotedatasource.usecase.data.CartItem
import com.roland.android.remotedatasource.usecase.data.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ListViewModel : ViewModel(), KoinComponent {
	private val getCategoryUseCase: GetCategoryUseCase by inject()

	private val _listUiState = MutableStateFlow(ListUiState())
	var listUiState by mutableStateOf(_listUiState.value)
	var cartItems by mutableStateOf<List<CartItem>>(emptyList())
	var favoriteItems by mutableStateOf<List<Item>>(emptyList())
	var categoryId by mutableStateOf("")

	init {
		viewModelScope.launch {
			cartItemsFlow.collect {
				cartItems = it
			}
		}
		viewModelScope.launch {
			favoriteItemsFlow.collect {
				favoriteItems = it
				_listUiState.update { it.copy(favoriteItems = favoriteItems) }
			}
		}
		viewModelScope.launch {
			_listUiState.collect {
				listUiState = it
			}
		}
	}

	fun fetchData(categoryListId: String) {
		categoryId = categoryListId
		viewModelScope.launch {
			getCategoryUseCase.execute(GetCategoryUseCase.Request(categoryListId))
				.map { convertToCategoryData(it) }
				.collect { data ->
					_listUiState.update { it.copy(data = data) }
				}
		}
	}

	fun actions(action: ListActions) {
		when (action) {
			is ListActions.AddToCart -> addToCart(action.item, action.color, action.size)
			is ListActions.Favorite -> favorite(action.item, action.favorite)
			ListActions.Reload -> reload()
		}
	}

	private fun addToCart(
		item: Item,
		color: Long,
		size: Int
	) {
		val numberInCart = cartItems.filter { it.id == item.id }.size
		cartItemsFlow.value = cartItems + item.toCartItem(color, size, numberInCart)
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
		_listUiState.update { it.copy(data = null) }
		fetchData(categoryId)
	}

}