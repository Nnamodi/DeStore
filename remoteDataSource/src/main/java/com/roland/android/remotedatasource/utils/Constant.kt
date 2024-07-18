package com.roland.android.remotedatasource.utils

import android.content.Context
import com.roland.android.remotedatasource.R

object Constant {

	internal const val TIMBU_BASE_URL = "https://api.timbu.cloud"
	const val BASE_IMAGE_URL = "https://api.timbu.cloud/images/"

	fun Throwable.errorMessage(context: Context): String = when (this) {
		is java.net.UnknownHostException -> context.getString(R.string.no_internet_connection)
		is javax.net.ssl.SSLException -> context.getString(R.string.connection_aborted)
		else -> context.getString(R.string.oops_something_broke)
	}

}