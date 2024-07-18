package com.roland.android.domain.di

import com.roland.android.domain.usecase.GetCategoryUseCase
import com.roland.android.domain.usecase.GetProductUseCase
import com.roland.android.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

object DomainModule {

	private fun provideCoroutineScope(): CoroutineScope {
		return CoroutineScope(Dispatchers.IO)
	}

	val domainModule = module {
		single { provideCoroutineScope() }
		single { GetCategoryUseCase() }
		single { GetProductUseCase() }
		single { GetProductsUseCase() }
	}

}