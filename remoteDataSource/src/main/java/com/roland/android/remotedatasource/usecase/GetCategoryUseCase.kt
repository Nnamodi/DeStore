package com.roland.android.remotedatasource.usecase

import com.roland.android.remotedatasource.repository.ProductsRepository
import com.roland.android.remotedatasource.usecase.data.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetCategoryUseCase : UseCase<GetCategoryUseCase.Request, GetCategoryUseCase.Response>(), KoinComponent {

	private val productsRepository: ProductsRepository by inject()

	override fun process(request: Request): Flow<Response> {
		return (if (request.categoryId == null) {
			productsRepository.fetchItems()
		} else {
			productsRepository.fetchItemsByCategory(request.categoryId)
		}).map {
			Response(it)
		}
	}

	data class Request(val categoryId: String?) : UseCase.Request

	data class Response(val data: List<Item>) : UseCase.Response

}