package com.roland.android.destore.ui.screen.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.destore.data.allItemsFlow
import com.roland.android.destore.data.cartItemsFlow
import com.roland.android.destore.data.userInfoFlow
import com.roland.android.destore.data.wishlistItemsFlow
import com.roland.android.destore.ui.components.Colors
import com.roland.android.destore.ui.screen.details.Sizes
import com.roland.android.destore.utils.Extensions.toCartItem
import com.roland.android.destore.utils.ResponseConverter.convertToHomeData
import com.roland.android.domain.data.CartItem
import com.roland.android.domain.data.Item
import com.roland.android.domain.data.State
import com.roland.android.domain.usecase.CartUseCase
import com.roland.android.domain.usecase.CartUseCaseActions
import com.roland.android.domain.usecase.GetProductsUseCase
import com.roland.android.domain.usecase.UserUseCase
import com.roland.android.domain.usecase.UserUseCaseActions
import com.roland.android.domain.usecase.WishlistUseCase
import com.roland.android.domain.usecase.WishlistUseCaseActions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel : ViewModel(), KoinComponent {
	private val getProductsUseCase: GetProductsUseCase by inject()
	private val cartUseCase: CartUseCase by inject()
	private val favoriteUseCase: WishlistUseCase by inject()
	private val userUseCase: UserUseCase by inject()

	private val _homeUiState = MutableStateFlow(HomeUiState())
	var homeUiState by mutableStateOf(_homeUiState.value); private set
	private var allItems by mutableStateOf<List<Item>>(emptyList())
	private var cartItems by mutableStateOf<List<CartItem>>(emptyList())

	init {
		fetchData()
		getUserInfo()

		viewModelScope.launch {
			allItemsFlow.collect { items ->
				allItems = items
			}
		}
		viewModelScope.launch {
			cartItemsFlow.collect {
				cartItems = it
			}
		}
		viewModelScope.launch {
			wishlistItemsFlow.collect { items ->
				_homeUiState.update { it.copy(wishlistItems = items) }
			}
		}
		viewModelScope.launch {
			userInfoFlow.collect { userInfo ->
				_homeUiState.update {
					it.copy(userName = userInfo.name)
				}
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
			favoriteUseCase.execute(
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

	private fun getUserInfo() {
		viewModelScope.launch {
			userUseCase.execute(
				UserUseCase.Request(
					UserUseCaseActions.GetUserInfo
				)
			)
				.collect { data ->
					Log.i("UserInfo", "$data")
					if (data !is State.Success) return@collect
					userInfoFlow.value = data.data.userInfo
				}
		}
	}

	private fun reload() {
		_homeUiState.update { it.copy(data = null) }
		fetchData()
	}

}