package com.roland.android.remotedatasource.network.service

import com.roland.android.remotedatasource.BuildConfig
import com.roland.android.remotedatasource.network.model.ItemDetailsModel
import com.roland.android.remotedatasource.network.model.ItemModel
import com.roland.android.remotedatasource.network.model.ListModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ProductsService {

	@GET("/products")
	suspend fun fetchItems(
		@Query("organization_id") orgId: String = BuildConfig.ORGANIZATION_ID,
		@Query("Appid") appId: String = BuildConfig.APP_ID,
		@Query("Apikey") apiKey: String = BuildConfig.API_KEY,
		@Query("size") size: Int = 100
	): ListModel

	@GET("/products{product_id}")
	suspend fun fetchItem(
		@Path("product_id") productId: String,
		@Query("organization_id") orgId: String = BuildConfig.ORGANIZATION_ID,
		@Query("Appid") appId: String = BuildConfig.APP_ID,
		@Query("Apikey") apiKey: String = BuildConfig.API_KEY,
	): ItemDetailsModel

	@GET("/products")
	suspend fun fetchItemsByCategory(
		@Query("category_id") categoryId: String,
		@Query("organization_id") orgId: String = BuildConfig.ORGANIZATION_ID,
		@Query("Appid") appId: String = BuildConfig.APP_ID,
		@Query("Apikey") apiKey: String = BuildConfig.API_KEY,
		@Query("size") size: Int = 100
	): ListModel

}