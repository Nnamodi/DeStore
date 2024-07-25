package com.roland.android.domain

import com.roland.android.domain.data.CartItem
import com.roland.android.domain.data.Order
import com.roland.android.domain.data.OrderDetails
import com.roland.android.domain.data.UserInfo
import com.roland.android.domain.repository.local.CartRepository
import com.roland.android.domain.repository.local.OrdersRepository
import com.roland.android.domain.repository.local.UserRepository
import com.roland.android.domain.repository.local.WishlistRepository
import com.roland.android.domain.usecase.CartUseCase
import com.roland.android.domain.usecase.CartUseCaseActions
import com.roland.android.domain.usecase.OrdersUseCase
import com.roland.android.domain.usecase.OrdersUseCaseActions
import com.roland.android.domain.usecase.UserUseCase
import com.roland.android.domain.usecase.UserUseCaseActions
import com.roland.android.domain.usecase.WishlistUseCase
import com.roland.android.domain.usecase.WishlistUseCaseActions
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DatabaseUseCaseTest {

	private val cartRepository = mock<CartRepository>()
	private val cartUseCase = mock<CartUseCase>()

	private val ordersRepository = mock<OrdersRepository>()
	private val ordersUseCase = mock<OrdersUseCase>()

	private val wishlistRepository = mock<WishlistRepository>()
	private val wishlistUseCase = mock<WishlistUseCase>()

	private val userRepository = mock<UserRepository>()
	private val userUseCase = mock<UserUseCase>()

	@Test
	fun testCartProcess() = runTest {
		val cartItem = CartItem()
		val cartItems = listOf(cartItem)
		whenever(cartRepository.getCartItems()).thenReturn(flowOf(cartItems))
		whenever(cartRepository.addToCart(cartItem)).thenReturn(flowOf(cartItems))
		whenever(cartRepository.removeFromCart(cartItem)).thenReturn(flowOf(cartItems))
		whenever(cartRepository.removeItemsFromCart(cartItems)).thenReturn(flowOf(cartItems))

		val response = cartUseCase.process(CartUseCase.Request(CartUseCaseActions.GetCartItems)).first()
		assertEquals(
			/* expected = */ CartUseCase.Response(cartItems),
			/* actual = */ response
		)
	}

	@Test
	fun testOrderProcess() = runTest {
		val orders = listOf(Order())
		val order = OrderDetails()
		whenever(ordersRepository.getOrderHistory()).thenReturn(flowOf(orders))
		whenever(ordersRepository.getOrder("")).thenReturn(flowOf(order))
		whenever(ordersRepository.saveOrder(order)).thenReturn(flowOf(orders))

		val response = ordersUseCase.process(OrdersUseCase.Request(OrdersUseCaseActions.GetOrderHistory)).first()
		assertEquals(
			/* expected = */ OrdersUseCase.Response(orders, order),
			/* actual = */ response
		)
	}

	@Test
	fun testWishlistProcess() = runTest {
		val wishlistItem = ""
		val wishlist = listOf(wishlistItem)
		whenever(wishlistRepository.getAllItems()).thenReturn(flowOf(wishlist))
		whenever(wishlistRepository.addToWishlist(wishlistItem)).thenReturn(flowOf(wishlist))
		whenever(wishlistRepository.removeFromWishlist(wishlistItem)).thenReturn(flowOf(wishlist))

		val response = wishlistUseCase.process(WishlistUseCase.Request(WishlistUseCaseActions.GetWishlistItems)).first()
		assertEquals(
			/* expected = */ WishlistUseCase.Response(wishlist),
			/* actual = */ response
		)
	}

	@Test
	fun testUserProcess() = runTest {
		val userInfo = UserInfo()
		whenever(userRepository.getUserInfo()).thenReturn(flowOf(userInfo))
		whenever(userRepository.updateUserInfo(userInfo)).thenReturn(flowOf(userInfo))

		val response = userUseCase.process(UserUseCase.Request(UserUseCaseActions.GetUserInfo)).first()
		assertEquals(
			/* expected = */ UserUseCase.Response(userInfo),
			/* actual = */ response
		)
	}

}