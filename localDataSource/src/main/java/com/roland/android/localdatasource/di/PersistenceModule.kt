package com.roland.android.localdatasource.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.roland.android.domain.repository.local.CartRepository
import com.roland.android.domain.repository.local.OrdersRepository
import com.roland.android.domain.repository.local.UserRepository
import com.roland.android.domain.repository.local.UtilRepository
import com.roland.android.domain.repository.local.WishlistRepository
import com.roland.android.localdatasource.database.AppDatabase
import com.roland.android.localdatasource.database.CartDao
import com.roland.android.localdatasource.database.OrdersDao
import com.roland.android.localdatasource.database.WishlistDao
import com.roland.android.localdatasource.datastore.UserDataStore
import com.roland.android.localdatasource.datastore.UtilDataStore
import com.roland.android.localdatasource.repository.CartRepositoryImpl
import com.roland.android.localdatasource.repository.OrdersRepositoryImpl
import com.roland.android.localdatasource.repository.UserRepositoryImpl
import com.roland.android.localdatasource.repository.UtilRepositoryImpl
import com.roland.android.localdatasource.repository.WishlistRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

object PersistenceModule {

	private fun provideAppDatabase(context: Context): AppDatabase {
		return Room.databaseBuilder(
			context = context,
			klass = AppDatabase::class.java,
			name = "app-database"
		).build()
	}

	private fun provideDataStore(context: Context): DataStore<Preferences> {
		return context.datastore
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

	private fun provideCoroutineScope(): CoroutineScope {
		return CoroutineScope(Dispatchers.IO)
	}

	val persistenceModule = module {
		single { provideAppDatabase(get<Context>().applicationContext) }
		single { provideDataStore(get<Context>().applicationContext) }
		single { provideCartDao(get()) }
		single { provideOrdersDao(get()) }
		single { provideWishlistDao(get()) }
		single { provideCoroutineScope() }
		single { UserDataStore() }
		single { UtilDataStore() }
		factory<CartRepository> { CartRepositoryImpl() }
		factory<OrdersRepository> { OrdersRepositoryImpl() }
		factory<WishlistRepository> { WishlistRepositoryImpl() }
		factory<UserRepository> { UserRepositoryImpl() }
		factory<UtilRepository> { UtilRepositoryImpl() }
	}

}