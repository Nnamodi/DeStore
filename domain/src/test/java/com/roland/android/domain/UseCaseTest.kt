package com.roland.android.domain

import com.roland.android.domain.usecase.UseCase
import com.roland.android.domain.data.State
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class UseCaseTest {

	private val request = mock<UseCase.Request>()
	private val response = mock<UseCase.Response>()

	private lateinit var useCase: UseCase<UseCase.Request, UseCase.Response>

	@Before
	fun setUp() {
		useCase = object : UseCase<UseCase.Request, UseCase.Response>() {
			override fun process(request: Request): Flow<Response> {
				assertEquals(this@UseCaseTest.request, request)
				return flowOf(response)
			}
		}
	}

	@Test
	fun testExecuteSuccess() = runTest {
		val result = useCase.execute(request).first()
		assertEquals(State.Success(response), result)
	}

}