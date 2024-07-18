package com.roland.android.domain.data

sealed class State<out T: Any> {

	data class Success<out T: Any>(val data: T) : State<T>()

	data class Error(val throwable: Throwable) : State<Nothing>()

}
