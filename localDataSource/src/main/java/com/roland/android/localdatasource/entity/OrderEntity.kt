package com.roland.android.localdatasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class OrderEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val orderNo: String,
	val dateOfPurchase: Date,
	val amount: Float
)

@Entity
data class OrderItemEntity(
	@PrimaryKey(autoGenerate = true)
	val generatedId: Int = 0,
	var orderNo: String = "",
	val id: String,
	val name: String,
	val photoUrl: String,
	val currentPrice: String,
	val color: Long,
	var size: Int
)
