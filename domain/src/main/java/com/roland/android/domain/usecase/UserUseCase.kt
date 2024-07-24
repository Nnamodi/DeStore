package com.roland.android.domain.usecase

import com.roland.android.domain.data.UserInfo
import com.roland.android.domain.repository.local.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserUseCase : UseCase<UserUseCase.Request, UserUseCase.Response>(), KoinComponent {

	private val userRepository: UserRepository by inject()

	override fun process(request: Request): Flow<Response> {
		val actionFlow = when (request.action) {
			UserUseCaseActions.GetUserInfo -> userRepository.getUserInfo()
			is UserUseCaseActions.UpdateUserInfo -> userRepository.updateUserInfo(request.action.userInfo)
		}
		return actionFlow.map { Response(it) }
	}

	data class Request(val action: UserUseCaseActions) : UseCase.Request

	data class Response(val userInfo: UserInfo) : UseCase.Response

}

sealed class UserUseCaseActions {

	data object GetUserInfo : UserUseCaseActions()

	data class UpdateUserInfo(val userInfo: UserInfo) : UserUseCaseActions()

}