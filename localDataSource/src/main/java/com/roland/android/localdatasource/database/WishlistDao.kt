package com.roland.android.localdatasource.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.roland.android.localdatasource.entity.WishlistItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {

	@Query("SELECT * FROM wishlist_item")
	fun getAllItems(): Flow<List<WishlistItemEntity>>

	@Insert
	suspend fun addToWishlist(item: WishlistItemEntity)

	@Delete
	suspend fun removeFromWishlist(item: WishlistItemEntity)

}