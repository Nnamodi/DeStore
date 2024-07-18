package com.roland.android.localdatasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import kotlin.collections.List

@Entity(tableName = "order")
data class OrderEntity(
	@PrimaryKey
	val id: String = "",
	val orderIds: List<String> = emptyList(),
	val orderNames: List<String> = emptyList(),
	val orderPhotosUrl: List<String> = emptyList(),
	val orderPrices: List<String> = emptyList(),
	val orderColors: List<Long> = emptyList(),
	val orderSizes: List<Int> = emptyList(),
	val dateOfPurchase: Date = Date()
)
