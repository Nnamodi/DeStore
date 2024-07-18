package com.roland.android.localdatasource.repository

import com.roland.android.domain.repository.local.ItemId
import com.roland.android.domain.repository.local.WishlistRepository
import com.roland.android.localdatasource.database.WishlistDao
import com.roland.android.localdatasource.entity.WishlistItemEntity
import com.roland.android.localdatasource.utils.Converters.convertFromWishlist
import com.roland.android.localdatasource.utils.Converters.convertToWishlist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WishlistRepositoryImpl : WishlistRepository, KoinComponent {

	private val wishlistDao: WishlistDao by inject()
	private val coroutineScope: CoroutineScope by inject()

	override fun getAllItems(): Flow<List<ItemId>> {
		return wishlistDao.getAllItems().map { itemIds ->
			itemIds.map {
				convertToWishlist(it)
			}
		}
	}

	override fun addToWishlist(itemId: ItemId): Flow<List<ItemId>> {
		coroutineScope.launch {
			val item = convertFromWishlist(itemId)
			wishlistDao.addToWishlist(item)
		}
		return wishlistDao.getAllItems().mapWishlist()
	}

	override fun removeFromWishlist(itemId: ItemId): Flow<List<ItemId>> {
		coroutineScope.launch {
			val item = convertFromWishlist(itemId)
			wishlistDao.removeFromWishlist(item)
		}
		return wishlistDao.getAllItems().mapWishlist()
	}

	private fun Flow<List<WishlistItemEntity>>.mapWishlist(): Flow<List<ItemId>> {
		return map { wishlistItemEntities ->
			wishlistItemEntities.map {
				convertToWishlist(it)
			}
		}
	}

}