package com.roland.android.destore.ui.screen.sign_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.data.State
import com.roland.android.domain.data.UserInfo
import com.roland.android.domain.usecase.UserUseCase
import com.roland.android.domain.usecase.UserUseCaseActions
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SignUpViewModel : ViewModel(), KoinComponent {

	private val userUseCase: UserUseCase by inject()
	var userInfo by mutableStateOf(UserInfo()); private set

	init {
		getUserInfo()
	}

	private fun getUserInfo() {
		viewModelScope.launch {
			userUseCase.execute(
				UserUseCase.Request(
					UserUseCaseActions.GetUserInfo
				)
			)
				.collect { data ->
					if (data !is State.Success) return@collect
					userInfo = data.data.userInfo
				}
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

}