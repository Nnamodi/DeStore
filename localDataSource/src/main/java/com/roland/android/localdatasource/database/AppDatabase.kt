package com.roland.android.localdatasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.roland.android.localdatasource.entity.CartItemEntity
import com.roland.android.localdatasource.entity.OrderEntity
import com.roland.android.localdatasource.entity.WishlistItemEntity
import com.roland.android.localdatasource.utils.TypeConverter

@Database(
	entities = [CartItemEntity::class, OrderEntity::class, WishlistItemEntity::class],
	version = 1
)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

	abstract fun cartDao(): CartDao

	abstract fun ordersDao(): OrdersDao

	abstract fun wishlistDao(): WishlistDao

}