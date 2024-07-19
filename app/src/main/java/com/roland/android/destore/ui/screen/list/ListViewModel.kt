package com.roland.android.destore.ui.screen.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.destore.data.allItemsFlow
import com.roland.android.destore.data.cartItemsFlow
import com.roland.android.destore.data.wishlistItemsFlow
import com.roland.android.destore.utils.Extensions.toCartItem
import com.roland.android.destore.utils.ResponseConverter.convertToCategoryData
import com.roland.android.domain.data.CartItem
import com.roland.android.domain.data.Item
import com.roland.android.domain.data.State
import com.roland.android.domain.usecase.CartUseCase
import com.roland.android.domain.usecase.CartUseCaseActions
import com.roland.android.domain.usecase.GetCategoryUseCase
import com.roland.android.domain.usecase.WishlistUseCase
import com.roland.android.domain.usecase.WishlistUseCaseActions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ListViewModel : ViewModel(), KoinComponent {
	private val getCategoryUseCase: GetCategoryUseCase by inject()
	private val cartUseCase: CartUseCase by inject()
	private val wishlistUseCase: WishlistUseCase by inject()

	private val _listUiState = MutableStateFlow(ListUiState())
	var listUiState by mutableStateOf(_listUiState.value); private set
	private var allItems by mutableStateOf<List<Item>>(emptyList())
	private var cartItems by mutableStateOf<List<CartItem>>(emptyList())
	private var categoryId by mutableStateOf("")

	init {
		fetchAllData()

		viewModelScope.launch {
			allItemsFlow.collect { items ->
				allItems = items
				fetchWishlist()
			}
		}
		viewModelScope.launch {
			cartItemsFlow.collect {
				cartItems = it
			}
		}
		viewModelScope.launch {
			wishlistItemsFlow.collect { items ->
				_listUiState.update { it.copy(wishlistItems = items) }
			}
		}
		viewModelScope.launch {
			_listUiState.collect {
				listUiState = it
			}
		}
	}

	private fun fetchAllData() {
		if (listUiState.allItems is State.Success) return
		_listUiState.update { it.copy(allItems = null) }

		viewModelScope.launch {
			getCategoryUseCase.execute(GetCategoryUseCase.Request(null))
				.map { convertToCategoryData(it) }
				.collect { data ->
					_listUiState.update { it.copy(allItems = data) }
					if (data !is State.Success) return@collect
					allItemsFlow.value = data.data
				}
		}
	}

	fun fetchCategorizedData(categoryListId: String) {
		if (categoryListId == categoryId && listUiState.categorizedItems is State.Success) return

		_listUiState.update { it.copy(categorizedItems = null) }
		categoryId = categoryListId

		viewModelScope.launch {
			getCategoryUseCase.execute(GetCategoryUseCase.Request(categoryListId))
				.map { convertToCategoryData(it) }
				.collect { data ->
					_listUiState.update { it.copy(categorizedItems = data) }
				}
		}
	}

	private fun fetchWishlist() {
		viewModelScope.launch {
			wishlistUseCase.execute(
				WishlistUseCase.Request(
					WishlistUseCaseActions.GetWishlistItems
				)
			)
				.collect { data ->
					if (data !is State.Success) return@collect
					wishlistItemsFlow.value = allItems.filter { product ->
						product.id in data.data.wishlistItems.map { it }
					}
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
		viewModelScope.launch {
			cartUseCase.execute(
				CartUseCase.Request(
					CartUseCaseActions.AddToCart(item.toCartItem(color, size))
				)
			)
				.collect { data ->
					if (data !is State.Success) return@collect
					cartItemsFlow.value = data.data.cartItems
				}
		}
	}

	private fun favorite(
		item: Item,
		favorite: Boolean
	) {
		viewModelScope.launch {
			wishlistUseCase.execute(
				WishlistUseCase.Request(
					if (favorite) {
						WishlistUseCaseActions.AddToWishlist(item.id)
					} else {
						WishlistUseCaseActions.RemoveFromWishlist(item.id)
					}
				)
			)
				.collect { data ->
					if (data !is State.Success) return@collect
					wishlistItemsFlow.value = allItems.filter { product ->
						product.id in data.data.wishlistItems.map { it }
					}
				}
		}
	}

	private fun reload() {
		when {
			listUiState.allItems is State.Error -> {
				_listUiState.update { it.copy(allItems = null) }
				fetchAllData()
			}
			listUiState.categorizedItems is State.Error -> {
				_listUiState.update { it.copy(categorizedItems = null) }
				fetchCategorizedData(categoryId)
			}
		}
	}

}