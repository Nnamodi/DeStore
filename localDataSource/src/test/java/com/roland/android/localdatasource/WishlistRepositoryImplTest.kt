package com.roland.android.localdatasource

import com.roland.android.localdatasource.database.WishlistDao
import com.roland.android.localdatasource.entity.WishlistItemEntity
import com.roland.android.localdatasource.repository.WishlistRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class WishlistRepositoryImplTest {

	private val wishlistDao = mock<WishlistDao>()
	private val wishlistRepository = mock<WishlistRepositoryImpl>()

	@Test
	fun testGetAllItems() = runTest {
		val itemId = "0"
		val localWishlist = listOf(WishlistItemEntity(itemId))
		whenever(wishlistDao.getAllItems()).thenReturn(flowOf(localWishlist))

		val expectedWishlist = listOf(itemId)
		val savedWishlist = wishlistRepository.getAllItems().first()
		assertEquals(expectedWishlist, savedWishlist)
	}

	@Test
	fun testAddToWishlist() = runTest {
		val itemId = "0"
		val newWishlist = WishlistItemEntity(itemId)

		wishlistRepository.addToWishlist(itemId)
		verify(wishlistDao).addToWishlist(newWishlist)
	}

	@Test
	fun testRemoveFromWishlist() = runTest {
		val itemId = "0"
		val wishlistToRemove = WishlistItemEntity(itemId)

		wishlistRepository.removeFromWishlist(itemId)
		verify(wishlistDao).removeFromWishlist(wishlistToRemove)
	}

}