package com.roland.android.remotedatasource.usecase

import com.roland.android.remotedatasource.network.model.ItemModel
import com.roland.android.remotedatasource.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetProductsUseCase : UseCase<GetProductsUseCase.Request, GetProductsUseCase.Response>(), KoinComponent {

	private val productsRepository: ProductsRepository by inject()

	override fun process(request: Request): Flow<Response> {
		return productsRepository.fetchItems().map {
			Response(it)
		}
	}

	object Request : UseCase.Request

	data class Response(val data: List<ItemModel>) : UseCase.Response

}