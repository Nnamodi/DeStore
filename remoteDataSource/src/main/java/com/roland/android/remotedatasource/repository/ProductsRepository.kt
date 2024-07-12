package com.roland.android.remotedatasource.repository

import com.roland.android.remotedatasource.usecase.data.Item
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

	fun fetchItems(): Flow<List<Item>>

}