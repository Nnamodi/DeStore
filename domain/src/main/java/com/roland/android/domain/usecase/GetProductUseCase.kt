package com.roland.android.domain.usecase

import com.roland.android.domain.repository.remote.ProductsRepository
import com.roland.android.domain.data.ItemDetails
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