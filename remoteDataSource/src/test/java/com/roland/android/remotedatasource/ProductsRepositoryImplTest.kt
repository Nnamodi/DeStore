package com.roland.android.remotedatasource

import com.roland.android.domain.data.Item
import com.roland.android.remotedatasource.repository.ProductsRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ProductsRepositoryImplTest {

	private val productsRepository = mock<ProductsRepositoryImpl>()

	@Test
	fun testFetchItems() = runTest {
		whenever(productsRepository.fetchItems()).thenReturn(flowOf(listOf(Item())))

		val products = productsRepository.fetchItems().first()
		assertEquals(products, listOf(Item()))
	}

}