package com.roland.android.localdatasource.utils

import androidx.room.TypeConverter
import java.util.Date

private const val SEPARATOR = "|||"

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

	@TypeConverter
	fun fromListOfInt(list: List<Int>): String {
		return list.joinToString { SEPARATOR }
	}

	@TypeConverter
	fun toListOfInt(string: String): List<Int> {
		return string.split(SEPARATOR).map { it.toInt() }
	}

	@TypeConverter
	fun fromListOfLong(list: List<Long>): String {
		return list.joinToString { SEPARATOR }
	}

	@TypeConverter
	fun toListOfLong(string: String): List<Long> {
		return string.split(SEPARATOR).map { it.toLong() }
	}

	@TypeConverter
	fun fromListOfStrings(list: List<String>): String {
		return list.joinToString { SEPARATOR }
	}

	@TypeConverter
	fun toListOfString(string: String): List<String> {
		return string.split(SEPARATOR)
	}

}