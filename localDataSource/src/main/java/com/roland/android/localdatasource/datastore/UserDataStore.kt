package com.roland.android.localdatasource.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.roland.android.domain.data.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal val NAME_KEY = stringPreferencesKey("name")
internal val EMAIL_KEY = stringPreferencesKey("email")
internal val PHONE_KEY = stringPreferencesKey("phone")
internal val ADDRESS_KEY = stringPreferencesKey("address")

class UserDataStore : KoinComponent {

	private val datastore: DataStore<Preferences> by inject()

	fun getUserInfo(): Flow<UserInfo> {
		return datastore.data.map {
			UserInfo(
				name = it[NAME_KEY] ?: "Esteemed Customer",
				email = it[EMAIL_KEY] ?: "",
				phone = it[PHONE_KEY] ?: "",
				address = it[ADDRESS_KEY] ?: ""
			)
		}
	}

	suspend fun updateUserInfo(info: UserInfo) {
		datastore.edit {
			it[NAME_KEY] = info.name
			it[EMAIL_KEY] = info.email
			it[PHONE_KEY] = info.phone
			it[ADDRESS_KEY] = info.address
		}
	}

}
