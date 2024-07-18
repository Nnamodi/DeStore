package com.roland.android.localdatasource.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.roland.android.localdatasource.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

	@Query("SELECT * FROM cart_item")
	fun getCartItems(): Flow<List<CartItemEntity>>

	@Insert
	suspend fun addToCart(cartItem: CartItemEntity)

	@Delete
	suspend fun removeFromCart(cartItem: CartItemEntity)

}