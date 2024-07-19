package com.roland.android.localdatasource.utils

import androidx.room.TypeConverter
import com.roland.android.localdatasource.entity.OrderEntity
import java.util.Date

class TypeConverter {

	@TypeConverter
	fun fromDate(date: Date?): Long? {
		return date?.time
	}

	@TypeConverter
	fun toDate(millisSinceEpoch: Long?): Date? {
		return millisSinceEpoch?.let {
			Date(it)
		}
	}

}