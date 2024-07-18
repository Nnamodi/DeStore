package com.roland.android.localdatasource.di

import android.content.Context
import androidx.room.Room
import com.roland.android.domain.repository.local.CartRepository
import com.roland.android.domain.repository.local.OrdersRepository
import com.roland.android.domain.repository.local.WishlistRepository
import com.roland.android.localdatasource.database.AppDatabase
import com.roland.android.localdatasource.database.CartDao
import com.roland.android.localdatasource.database.OrdersDao
import com.roland.android.localdatasource.database.WishlistDao
import com.roland.android.localdatasource.repository.CartRepositoryImpl
import com.roland.android.localdatasource.repository.OrdersRepositoryImpl
import com.roland.android.localdatasource.repository.WishlistRepositoryImpl
import org.koin.dsl.module

object PersistenceModule {

	private fun provideAppDatabase(context: Context): AppDatabase {
		return Room.databaseBuilder(
			context = context,
			klass = AppDatabase::class.java,
			name = "app-database"
		).build()
	}

	private fun provideCartDao(appDatabase: AppDatabase): CartDao {
		return appDatabase.cartDao()
	}

	private fun provideOrdersDao(appDatabase: AppDatabase): OrdersDao {
		return appDatabase.ordersDao()
	}

	private fun provideWishlistDao(appDatabase: AppDatabase): WishlistDao {
		return appDatabase.wishlistDao()
	}

	val persistenceModule = module {
		single { provideAppDatabase(get<Context>().applicationContext) }
		single { provideCartDao(get()) }
		single { provideOrdersDao(get()) }
		single { provideWishlistDao(get()) }
		factory<CartRepository> { CartRepositoryImpl() }
		factory<OrdersRepository> { OrdersRepositoryImpl() }
		factory<WishlistRepository> { WishlistRepositoryImpl() }
	}

}