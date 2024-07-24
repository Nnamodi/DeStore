package com.roland.android.localdatasource.repository

import com.roland.android.domain.data.UserInfo
import com.roland.android.domain.repository.local.UserRepository
import com.roland.android.localdatasource.datastore.UserDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserRepositoryImpl : UserRepository, KoinComponent {

	private val userDataStore: UserDataStore by inject()
	private val coroutineScope: CoroutineScope by inject()

	override fun getUserInfo(): Flow<UserInfo> {
		return userDataStore.getUserInfo()
	}

	override fun updateUserInfo(info: UserInfo): Flow<UserInfo> {
		coroutineScope.launch {
			userDataStore.updateUserInfo(info)
		}
		return userDataStore.getUserInfo()
	}

}