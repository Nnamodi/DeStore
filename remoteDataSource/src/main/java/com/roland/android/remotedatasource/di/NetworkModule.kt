package com.roland.android.remotedatasource.di

import com.roland.android.remotedatasource.network.service.ProductsService
import com.roland.android.remotedatasource.repository.ProductsRepository
import com.roland.android.remotedatasource.repository.impl.ProductsRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class NetworkModule {

	fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
		.readTimeout(60, TimeUnit.SECONDS)
		.connectTimeout(60, TimeUnit.SECONDS)
		.build()

	fun provideMoshi(): Moshi = Moshi.Builder()
		.add(KotlinJsonAdapterFactory())
		.build()

	fun provideRetrofit(
		okHttpClient: OkHttpClient,
		moshi: Moshi
	): Retrofit = Retrofit.Builder()
		.baseUrl("https://api.timbu.cloud")
		.client(okHttpClient)
		.addConverterFactory(MoshiConverterFactory.create(moshi))
		.build()

	fun provideProductsService(retrofit: Retrofit): ProductsService {
		return retrofit.create(ProductsService::class.java)
	}

	val remoteDataModule = module {
		single { provideOkHttpClient() }
		single { provideMoshi() }
		single { provideRetrofit(get(), get()) }
		single { provideProductsService(get()) }
		factory<ProductsRepository> { ProductsRepositoryImpl() }
	}

}