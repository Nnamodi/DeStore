package com.roland.android.domain

import com.roland.android.domain.data.Item
import com.roland.android.domain.data.ItemDetails
import com.roland.android.domain.repository.remote.ProductsRepository
import com.roland.android.domain.usecase.GetCategoryUseCase
import com.roland.android.domain.usecase.GetProductUseCase
import com.roland.android.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetProductsUseCaseTest {

	private val productsRepository = mock<ProductsRepository>()
	private val categoryUseCase = mock<GetCategoryUseCase>()
	private val productUseCase = mock<GetProductUseCase>()
	private val productsUseCase = mock<GetProductsUseCase>()

	@Test
	fun testProductsProcess() = runTest {
		val items = listOf(Item())
		whenever(productsRepository.fetchItemsByCategory("")).thenReturn(flowOf(items))

		val response = productsUseCase.process(GetProductsUseCase.Request).first()
		assertEquals(
			/* expected = */ GetProductsUseCase.Response(items, items),
			/* actual = */ response
		)
	}

	@Test
	fun testCategoryProcess() = runTest {
		val items = listOf(Item())
		whenever(productsRepository.fetchItems()).thenReturn(flowOf(items))
		whenever(productsRepository.fetchItemsByCategory("")).thenReturn(flowOf(items))

		val response = categoryUseCase.process(GetCategoryUseCase.Request(null)).first()
		assertEquals(
			/* expected = */ GetCategoryUseCase.Response(items),
			/* actual = */ response
		)
	}

	@Test
	fun testProductProcess() = runTest {
		val item = ItemDetails()
		whenever(productsRepository.fetchItem("")).thenReturn(flowOf(item))

		val response = productUseCase.process(GetProductUseCase.Request("")).first()
		assertEquals(
			/* expected = */ GetProductUseCase.Response(item),
			/* actual = */ response
		)
	}

}