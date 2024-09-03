package com.roland.android.domain.usecase

import com.roland.android.domain.repository.local.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UtilUseCase : UseCase<UtilUseCase.Request, UtilUseCase.Response>(), KoinComponent {

	private val utilRepository: UtilRepository by inject()

	override fun process(request: Request): Flow<Response> {
		val actionFlow = when (request.action) {
			UtilUseCaseActions.GetLaunchStatus -> utilRepository.getLaunchStatus()
			UtilUseCaseActions.UpdateLaunchStatus -> utilRepository.updateLaunchStatus()
		}
		return actionFlow.map { Response(it) }
	}

	data class Request(val action: UtilUseCaseActions) : UseCase.Request

	data class Response(val status: Boolean) : UseCase.Response

}

sealed class UtilUseCaseActions {

	data object GetLaunchStatus : UtilUseCaseActions()

	data object UpdateLaunchStatus : UtilUseCaseActions()

}