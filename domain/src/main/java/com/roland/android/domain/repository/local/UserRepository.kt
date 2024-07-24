package com.roland.android.domain.repository.local

import com.roland.android.domain.data.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {

	fun getUserInfo(): Flow<UserInfo>

	fun updateUserInfo(info: UserInfo): Flow<UserInfo>

}