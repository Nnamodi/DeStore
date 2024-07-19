package com.roland.android.localdatasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.roland.android.localdatasource.entity.OrderEntity
import com.roland.android.localdatasource.entity.OrderItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdersDao {

	@Query("SELECT * FROM OrderEntity ORDER BY dateOfPurchase DESC")
	fun getOrderHistory(): Flow<List<OrderEntity>>

	@Query("SELECT * FROM OrderEntity WHERE orderNo LIKE :orderNo")
	fun getOrder(orderNo: String): Flow<OrderEntity>

	@Query("SELECT * FROM OrderItemEntity WHERE orderNo LIKE :orderNo")
	fun getOrderItems(orderNo: String): Flow<List<OrderItemEntity>>

	@Insert
	suspend fun saveOrder(orderEntity: OrderEntity)

	@Insert
	suspend fun saveOrderItems(orderItems: List<OrderItemEntity>)

}