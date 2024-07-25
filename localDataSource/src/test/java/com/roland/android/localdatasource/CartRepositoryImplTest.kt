package com.roland.android.localdatasource

import com.roland.android.domain.data.CartItem
import com.roland.android.localdatasource.database.CartDao
import com.roland.android.localdatasource.entity.CartItemEntity
import com.roland.android.localdatasource.repository.CartRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CartRepositoryImplTest {

	private val cartDao = mock<CartDao>()
	private val cartRepository = mock<CartRepositoryImpl>()

	@Test
	fun testGetAllItems() = runTest {
		val localCartItems = listOf(CartItemEntity())
		whenever(cartDao.getCartItems()).thenReturn(flowOf(localCartItems))

		val expectedCartItems = listOf(CartItem())
		val savedCartItems = cartRepository.getCartItems().first()
		assertEquals(expectedCartItems, savedCartItems)
	}

	@Test
	fun testAddToCart() = runTest {
		cartRepository.addToCart(CartItem())
		verify(cartDao).addToCart(CartItemEntity())
	}

	@Test
	fun testRemoveFromCart() = runTest {
		cartRepository.removeFromCart(CartItem())
		verify(cartDao).removeFromCart(CartItemEntity())
	}

}