package com.roland.android.destore.ui.screen.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.destore.data.cartItemsFlow
import com.roland.android.destore.data.favoriteItemsFlow
import com.roland.android.destore.utils.Extensions.toCartItem
import com.roland.android.destore.utils.ResponseConverter.convertToCategoryData
import com.roland.android.destore.utils.ResponseConverter.convertToDetailsData
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

class DetailsViewModel : ViewModel(), KoinComponent {
	private val getProductUseCase: GetProductUseCase by inject()
	private val getCategoryUseCase: GetCategoryUseCase by inject()

	private val _detailsUiState = MutableStateFlow(DetailsUiState())
	var detailsUiState by mutableStateOf(_detailsUiState.value)
	private var cartItems by mutableStateOf<List<CartItem>>(emptyList())
	var favoriteItems by mutableStateOf<List<Item>>(emptyList())
	private var productId by mutableStateOf("")

	init {
		viewModelScope.launch {
			cartItemsFlow.collect { items ->
				cartItems = items
				val numberInCart = cartItems.filter { it.id == productId }.size
				val favorited = favoriteItems.any { it.id == productId }
				_detailsUiState.update {
					it.copy(
						favorited = favorited,
						numberInCart = numberInCart,
						favoriteItems = favoriteItems
					)
				}
			}
		}
		viewModelScope.launch {
			favoriteItemsFlow.collect {
				favoriteItems = it
				_detailsUiState.update { it.copy(favoriteItems = favoriteItems) }
			}
		}
		viewModelScope.launch {
			_detailsUiState.collect {
				detailsUiState = it
			}
		}
	}

	fun fetchDetails(itemId: String) {
		productId = itemId
		viewModelScope.launch {
			getProductUseCase.execute(GetProductUseCase.Request(itemId))
				.map { convertToDetailsData(it) }
				.collect { data ->
					_detailsUiState.update { it.copy(details = data) }
					if (data !is State.Success) return@collect
					fetchMoreInStore(data.data.categories.firstOrNull()?.id)
				}
		}
	}

	private fun fetchMoreInStore(categoryId: String?) {
		viewModelScope.launch {
			getCategoryUseCase.execute(GetCategoryUseCase.Request(categoryId))
				.map { convertToCategoryData(it) }
				.collect { data ->
					_detailsUiState.update { it.copy(moreInStore = data) }
				}
		}
	}

	fun actions(action: DetailsActions) {
		when (action) {
			is DetailsActions.AddToCart -> addToCart(action.item, action.color, action.size)
			is DetailsActions.Favorite -> favorite(action.item, action.favorite)
			DetailsActions.Reload -> reload()
			DetailsActions.ReloadCategoryList -> reloadCategoryList()
			is DetailsActions.RemoveFromCart -> removeFromCart(action.item)
		}
	}

	private fun addToCart(
		item: Item,
		color: Long,
		size: Int
	) {
		cartItemsFlow.value = cartItems + item.toCartItem(color, size)
	}

	private fun removeFromCart(item: Item) {
		val itemsInCart = cartItems.filter { it.id == item.id }
		cartItemsFlow.value = cartItems - itemsInCart.last()
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
		_detailsUiState.update { it.copy(details = null, moreInStore = null) }
		fetchDetails(productId)
	}

	private fun reloadCategoryList() {
		_detailsUiState.update { it.copy(moreInStore = null) }
		val categoryId = detailsUiState.category
		fetchMoreInStore(categoryId)
	}

}