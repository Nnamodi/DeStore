package com.roland.android.destore.ui.screen.sign_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.destore.data.userInfoFlow
import com.roland.android.destore.ui.navigation.AppRoute
import com.roland.android.domain.data.State
import com.roland.android.domain.data.UserInfo
import com.roland.android.domain.usecase.UserUseCase
import com.roland.android.domain.usecase.UserUseCaseActions
import com.roland.android.domain.usecase.UtilUseCase
import com.roland.android.domain.usecase.UtilUseCaseActions
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SignUpViewModel : ViewModel(), KoinComponent {
	private val userUseCase: UserUseCase by inject()
	private val utilUseCase: UtilUseCase by inject()

	var userInfo by mutableStateOf(UserInfo()); private set
	var startRoute by mutableStateOf(AppRoute.HomeScreen.route); private set

	init {
		viewModelScope.launch {
			utilUseCase.execute(
				UtilUseCase.Request(
					UtilUseCaseActions.GetLaunchStatus
				)
			)
				.collect { data ->
					if (data !is State.Success) return@collect
					startRoute = if (data.data.status) {
						AppRoute.HomeScreen.route
					} else {
						AppRoute.SignUpScreen.route
					}
				}
		}
		viewModelScope.launch {
			userInfoFlow.collect { userInfo = it }
		}
	}

	fun updateUserInfo(info: UserInfo) {
		viewModelScope.launch {
			userUseCase.execute(
				UserUseCase.Request(
					UserUseCaseActions.UpdateUserInfo(info)
				)
			)
				.collect { data ->
					if (data !is State.Success) return@collect
					userInfo = data.data.userInfo
				}
		}
	}

	fun updateLaunchStatus() {
		viewModelScope.launch {
			utilUseCase.execute(
				UtilUseCase.Request(
					UtilUseCaseActions.UpdateLaunchStatus
				)
			)
		}
	}

}