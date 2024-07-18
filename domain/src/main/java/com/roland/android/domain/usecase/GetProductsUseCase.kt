package com.roland.android.domain.usecase

import com.roland.android.domain.repository.remote.ProductsRepository
import com.roland.android.domain.data.Item
import com.roland.android.domain.Constant.FEATURED_CATEGORY
import com.roland.android.domain.Constant.SPECIALS_CATEGORY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetProductsUseCase : UseCase<GetProductsUseCase.Request, GetProductsUseCase.Response>(), KoinComponent {

	private val productsRepository: ProductsRepository by inject()

	override fun process(request: Request): Flow<Response> = combine (
		productsRepository.fetchItemsByCategory(FEATURED_CATEGORY),
		productsRepository.fetchItemsByCategory(SPECIALS_CATEGORY)
	) { featured, specials ->
		Response(featured, specials)
	}

	object Request : UseCase.Request

	data class Response(
		val featured: List<Item>,
		val specialOffers: List<Item>
	) : UseCase.Response

}