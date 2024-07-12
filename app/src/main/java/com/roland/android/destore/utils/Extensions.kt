package com.roland.android.destore.utils

import android.content.Context
import com.roland.android.outlet.R
import java.util.Calendar

object Extensions {

	fun greetings(context: Context): String {
		val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
		return when (currentTime) {
			in 0..11 -> context.getString(R.string.good_morning)
			in 12..15 -> context.getString(R.string.good_afternoon)
			in 16..20 -> context.getString(R.string.good_evening)
			else -> context.getString(R.string.good_night)
		}
	}

}