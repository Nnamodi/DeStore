package com.roland.android.remotedatasource.utils

import android.content.Context
import com.roland.android.remotedatasource.R

object Constant {

	internal const val TIMBU_BASE_URL = "https://api.timbu.cloud"
	const val BASE_IMAGE_URL = "https://api.timbu.cloud/images/"

	const val ADDIDAS_CATEGORY = "0021fd68e2364dffa0136e505974effd"
	const val NIKE_CATEGORY = "fa271fd4fe6d4eeda87b89e088ffa4d3"
	const val REEBOK_CATEGORY = "a2db823c2a524fde9571565239be8775"
	const val TIMBERLAND_CATEGORY = "10535c9365a2498583ec93462cb9bc5b"
	internal const val FEATURED_CATEGORY = "684bf2356ba149d293a024bfa93cbc85"
	internal const val SPECIALS_CATEGORY = "74cd578b4dbb415db61c05e2e1ee8eb4"

	fun Throwable.errorMessage(context: Context): String = when (this) {
		is java.net.UnknownHostException -> context.getString(R.string.no_internet_connection)
		is javax.net.ssl.SSLException -> context.getString(R.string.connection_aborted)
		else -> context.getString(R.string.oops_something_broke)
	}

}