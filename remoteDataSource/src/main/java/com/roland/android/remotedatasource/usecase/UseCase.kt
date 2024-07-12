package com.roland.android.remotedatasource.usecase

import android.util.Log
import com.roland.android.remotedatasource.utils.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

abstract class UseCase<I: UseCase.Request, O: UseCase.Response> {

	fun execute(request: I) = process(request)
		.map {
			State.Success(it) as State<O>
		}
		.catch {
			Log.i("DataInfo", it.toString())
			emit(State.Error(it))
		}

	internal abstract fun process(request: I): Flow<O>

	interface Request

	interface Response

}