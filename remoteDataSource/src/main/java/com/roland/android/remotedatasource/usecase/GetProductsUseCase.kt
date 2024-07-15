package com.roland.android.remotedatasource.usecase

import com.roland.android.remotedatasource.repository.ProductsRepository
import com.roland.android.remotedatasource.usecase.data.Item
import com.roland.android.remotedatasource.utils.Constant.FEATURED_CATEGORY
import com.roland.android.remotedatasource.utils.Constant.SPECIALS_CATEGORY
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