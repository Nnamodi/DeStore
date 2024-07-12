package com.roland.android.remotedatasource.utils

sealed class State<out T: Any> {

	data class Success<out T: Any>(val data: T) : State<T>()

	data class Error(val throwable: Throwable) : State<Nothing>()

}
