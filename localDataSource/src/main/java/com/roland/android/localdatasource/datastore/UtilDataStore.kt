package com.roland.android.localdatasource.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal val FIRST_LAUNCH_KEY = booleanPreferencesKey("first_launch")

class UtilDataStore : KoinComponent {

	private val datastore: DataStore<Preferences> by inject()

	fun getLaunchStatus(): Flow<Boolean> {
		return datastore.data.map {
			it[FIRST_LAUNCH_KEY] ?: false
		}
	}

	suspend fun updateLaunchStatus(launched: Boolean) {
		datastore.edit {
			it[FIRST_LAUNCH_KEY] = launched
		}
	}

}