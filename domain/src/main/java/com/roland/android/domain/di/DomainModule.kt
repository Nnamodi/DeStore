package com.roland.android.domain.di

import com.roland.android.domain.usecase.CartUseCase
import com.roland.android.domain.usecase.GetCategoryUseCase
import com.roland.android.domain.usecase.GetProductUseCase
import com.roland.android.domain.usecase.GetProductsUseCase
import com.roland.android.domain.usecase.OrdersUseCase
import com.roland.android.domain.usecase.UserUseCase
import com.roland.android.domain.usecase.UtilUseCase
import com.roland.android.domain.usecase.WishlistUseCase
import org.koin.dsl.module

object DomainModule {

	val domainModule = module {
		single { CartUseCase() }
		single { GetCategoryUseCase() }
		single { GetProductUseCase() }
		single { GetProductsUseCase() }
		single { OrdersUseCase() }
		single { UserUseCase() }
		single { UtilUseCase() }
		single { WishlistUseCase() }
	}

}