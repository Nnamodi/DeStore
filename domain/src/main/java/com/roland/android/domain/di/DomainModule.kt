package com.roland.android.domain.di

import com.roland.android.domain.usecase.CartUseCase
import com.roland.android.domain.usecase.GetCategoryUseCase
import com.roland.android.domain.usecase.GetProductUseCase
import com.roland.android.domain.usecase.GetProductsUseCase
import com.roland.android.domain.usecase.OrdersUseCase
import com.roland.android.domain.usecase.WishlistUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

object DomainModule {

	private fun provideCoroutineScope(): CoroutineScope {
		return CoroutineScope(Dispatchers.IO)
	}

	val domainModule = module {
		single { provideCoroutineScope() }
		single { CartUseCase() }
		single { GetCategoryUseCase() }
		single { GetProductUseCase() }
		single { GetProductsUseCase() }
		single { OrdersUseCase() }
		single { WishlistUseCase() }
	}

}