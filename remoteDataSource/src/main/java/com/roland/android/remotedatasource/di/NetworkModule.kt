package com.roland.android.remotedatasource.di

import com.roland.android.remotedatasource.utils.Constant.TIMBU_BASE_URL
import com.roland.android.remotedatasource.network.service.ProductsService
import com.roland.android.remotedatasource.repository.ProductsRepository
import com.roland.android.remotedatasource.repository.impl.ProductsRepositoryImpl
import com.roland.android.remotedatasource.usecase.GetCategoryUseCase
import com.roland.android.remotedatasource.usecase.GetProductUseCase
import com.roland.android.remotedatasource.usecase.GetProductsUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {

	private fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
		.readTimeout(60, TimeUnit.SECONDS)
		.connectTimeout(60, TimeUnit.SECONDS)
		.build()

	private fun provideMoshi(): Moshi = Moshi.Builder()
		.add(KotlinJsonAdapterFactory())
		.build()

	private fun provideRetrofit(
		okHttpClient: OkHttpClient,
		moshi: Moshi
	): Retrofit = Retrofit.Builder()
		.baseUrl(TIMBU_BASE_URL)
		.client(okHttpClient)
		.addConverterFactory(MoshiConverterFactory.create(moshi))
		.build()

	private fun provideProductsService(retrofit: Retrofit): ProductsService {
		return retrofit.create(ProductsService::class.java)
	}

	val remoteDataModule = module {
		single { provideOkHttpClient() }
		single { provideMoshi() }
		single { provideRetrofit(get(), get()) }
		single { provideProductsService(get()) }
		single { GetCategoryUseCase() }
		single { GetProductUseCase() }
		single { GetProductsUseCase() }
		factory<ProductsRepository> { ProductsRepositoryImpl() }
	}

}