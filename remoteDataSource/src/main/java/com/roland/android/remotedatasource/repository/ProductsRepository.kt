package com.roland.android.remotedatasource.repository

import com.roland.android.remotedatasource.usecase.data.Item
import com.roland.android.remotedatasource.usecase.data.ItemDetails
import kotlinx.coroutines.flow.Flow

internal interface ProductsRepository {

	fun fetchItems(): Flow<List<Item>>

	fun fetchItem(itemId: String): Flow<ItemDetails>

	fun fetchItemsByCategory(categoryId: String): Flow<List<Item>>

}