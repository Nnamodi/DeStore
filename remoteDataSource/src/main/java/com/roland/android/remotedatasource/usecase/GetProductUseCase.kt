package com.roland.android.remotedatasource.usecase

import com.roland.android.remotedatasource.network.model.ItemModel
import com.roland.android.remotedatasource.repository.ProductsRepository
import com.roland.android.remotedatasource.usecase.data.Item
import com.roland.android.remotedatasource.usecase.data.ItemDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetProductUseCase : UseCase<GetProductUseCase.Request, GetProductUseCase.Response>(), KoinComponent {

	private val productsRepository: ProductsRepository by inject()

	override fun process(request: Request): Flow<Response> {
		return productsRepository.fetchItem(request.itemId).map {
			Response(it)
		}
	}

	data class Request(val itemId: String) : UseCase.Request

	data class Response(val details: ItemDetails) : UseCase.Response

}