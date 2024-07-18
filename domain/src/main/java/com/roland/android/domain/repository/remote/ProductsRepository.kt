package com.roland.android.domain.repository.remote

import com.roland.android.domain.data.Item
import com.roland.android.domain.data.ItemDetails
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

	fun fetchItems(): Flow<List<Item>>

	fun fetchItem(itemId: String): Flow<ItemDetails>

	fun fetchItemsByCategory(categoryId: String): Flow<List<Item>>

}