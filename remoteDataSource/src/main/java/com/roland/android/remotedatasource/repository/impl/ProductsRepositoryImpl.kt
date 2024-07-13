package com.roland.android.remotedatasource.repository.impl

import com.roland.android.remotedatasource.network.service.ProductsService
import com.roland.android.remotedatasource.repository.ProductsRepository
import com.roland.android.remotedatasource.usecase.data.Item
import com.roland.android.remotedatasource.usecase.data.ItemDetails
import com.roland.android.remotedatasource.utils.Converters.convertToItemDetails
import com.roland.android.remotedatasource.utils.Converters.convertToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class ProductsRepositoryImpl : ProductsRepository, KoinComponent {

	private val productsService : ProductsService by inject()

	override fun fetchItems(): Flow<List<Item>> = flow {
		emit(productsService.fetchItems())
	}.map { convertToList(it).items }
		.catch { throw it }

	override fun fetchItem(itemId: String): Flow<ItemDetails> = flow {
		emit(productsService.fetchItem(itemId))
	}.map { convertToItemDetails(it) }
		.catch { throw it }

	override fun fetchItemsByCategory(categoryId: String): Flow<List<Item>> = flow {
		emit(
			productsService.fetchItemsByCategory(categoryId)
		)
	}.map { convertToList(it).items }
		.catch { throw it }

}