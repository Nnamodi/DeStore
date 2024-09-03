package com.roland.android.domain.repository.local

import kotlinx.coroutines.flow.Flow

interface UtilRepository {

	fun getLaunchStatus(): Flow<Boolean>

	fun updateLaunchStatus(): Flow<Boolean>

}