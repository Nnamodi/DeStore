package com.roland.android.localdatasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_item")
data class CartItemEntity(
	@PrimaryKey(autoGenerate = true)
	val generatedId: Int = 0,
	val id: String = "",
	val name: String = "",
	val photoUrl: String = "",
	val currentPrice: String = "",
	val color: Long = 0,
	var size: Int = 0
)

@Entity(tableName = "wishlist_item")
data class WishlistItemEntity(
	@PrimaryKey(autoGenerate = true)
	val generatedId: Int = 0,
	val itemId: String = ""
)
