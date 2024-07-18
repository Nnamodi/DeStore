package com.roland.android.destore.utils

import android.content.Context
import com.roland.android.destore.R
import com.roland.android.destore.ui.components.Colors
import com.roland.android.domain.data.CartItem
import com.roland.android.domain.data.Category
import com.roland.android.domain.data.Item
import com.roland.android.domain.data.ItemDetails
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Calendar

object Extensions {

	fun Context.greetings(): String {
		val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
		return when (currentTime) {
			in 0..11 -> getString(R.string.good_morning)
			in 12..15 -> getString(R.string.good_afternoon)
			in 16..20 -> getString(R.string.good_evening)
			else -> getString(R.string.good_night)
		}
	}

	fun Double.round(): Float {
		return BigDecimal(this)
			.setScale(2, RoundingMode.HALF_EVEN)
			.toFloat()
	}

	fun Long.getColor(): Colors {
		val color = Colors.entries.find { it.color.value == this.toULong() }
		return Colors.valueOf(color?.name ?: Colors.ColorBlue.name)
	}

	fun ItemDetails.toItem(price: String) =
		Item(
			id = id,
			name = name,
			photos = photos,
			currentPrice = price,
			category = categories.firstOrNull()
				?: Category(name = "Generic")
		)

	fun Item.toCartItem(
		color: Long,
		size: Int
	) = CartItem(
		id = id,
		name = name,
		photo = photos.last(),
		currentPrice = currentPrice,
		color = color,
		size = size
	)

	fun <T> List<T>.transformList(): List<List<T>> {
		var i = 0
		val list = mutableListOf<List<T>>()
		while (i < size) {
			val tList = mutableListOf<T>()
			tList.add(this[i])
			if (i + 1 < size) { tList.add(this[i + 1]) }
			list.add(tList.toList())
			i += if (i + 1 < size) 2 else 1
		}
		return list.toList()
	}

	fun List<CartItem>.filterCartItems(item: CartItem): List<CartItem> =
		filter {
			it.id == item.id &&
				it.color == item.color &&
					it.size == item.size
		}

}