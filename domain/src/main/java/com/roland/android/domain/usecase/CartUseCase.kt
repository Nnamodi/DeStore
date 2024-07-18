package com.roland.android.domain.usecase

import com.roland.android.domain.data.CartItem
import com.roland.android.domain.repository.local.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CartUseCase : UseCase<CartUseCase.Request, CartUseCase.Response>(), KoinComponent {

	private val cartRepository: CartRepository by inject()

	override fun process(request: Request): Flow<Response> {
		val actionFlow = when (request.action) {
			CartUseCaseActions.GetCartItems -> cartRepository.getCartItems()
			is CartUseCaseActions.AddToCart -> cartRepository.addToCart(request.action.cartItem)
			is CartUseCaseActions.RemoveFromCart -> cartRepository.removeFromCart(request.action.cartItem)
			is CartUseCaseActions.RemoveItemsFromCart -> cartRepository.removeItemsFromCart(request.action.cartItems)
		}
		return actionFlow.map { Response(it) }
	}

	data class Request(val action: CartUseCaseActions) : UseCase.Request

	data class Response(val cartItems: List<CartItem>) : UseCase.Response

}

sealed class CartUseCaseActions {

	data object GetCartItems : CartUseCaseActions()

	data class AddToCart(val cartItem: CartItem) : CartUseCaseActions()

	data class RemoveFromCart(val cartItem: CartItem) : CartUseCaseActions()

	data class RemoveItemsFromCart(val cartItems: List<CartItem>) : CartUseCaseActions()

}