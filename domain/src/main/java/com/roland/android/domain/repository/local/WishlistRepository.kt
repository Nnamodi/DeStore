package com.roland.android.domain.repository.local

import kotlinx.coroutines.flow.Flow

typealias ItemId = String

interface WishlistRepository {

	fun getAllItems(): Flow<List<ItemId>>

	fun addToWishlist(itemId: ItemId): Flow<List<ItemId>>

	fun removeFromWishlist(itemId: ItemId): Flow<List<ItemId>>

}