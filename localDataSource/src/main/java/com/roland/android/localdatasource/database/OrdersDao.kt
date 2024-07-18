package com.roland.android.localdatasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.roland.android.localdatasource.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdersDao {

	@Query("SELECT * FROM `order`")
	fun getOrderHistory(): Flow<List<OrderEntity>>

	@Query("SELECT * FROM `order` WHERE id=(:orderId)")
	fun getOrder(orderId: Int): Flow<OrderEntity>

	@Insert
	suspend fun saveOrder(order: OrderEntity)

}