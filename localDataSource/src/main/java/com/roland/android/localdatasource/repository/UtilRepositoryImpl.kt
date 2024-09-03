package com.roland.android.localdatasource.repository

import com.roland.android.domain.repository.local.UtilRepository
import com.roland.android.localdatasource.datastore.UtilDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UtilRepositoryImpl : UtilRepository, KoinComponent {

	private val utilDataStore: UtilDataStore by inject()
	private val coroutineScope: CoroutineScope by inject()

	override fun getLaunchStatus(): Flow<Boolean> {
		return utilDataStore.getLaunchStatus()
	}

	override fun updateLaunchStatus(): Flow<Boolean> {
		coroutineScope.launch {
			utilDataStore.updateLaunchStatus()
		}
		return utilDataStore.getLaunchStatus()
	}

}