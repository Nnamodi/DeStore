package com.roland.android.destore.utils

import android.content.Context
import com.roland.android.destore.R
import com.roland.android.destore.ui.components.Colors
import com.roland.android.remotedatasource.usecase.data.CartItem
import com.roland.android.remotedatasource.usecase.data.Item
import com.roland.android.remotedatasource.usecase.data.ItemDetails
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

	fun Long.getColor(): Colors {
		val color = Colors.entries.find { it.color.value == this.toULong() }
		return Colors.valueOf(color?.name ?: Colors.ColorBlue.name)
	}

	fun ItemDetails.toItem() = Item(
		id = id,
		name = name,
		photos = photos,
		currentPrice = currentPrice,
		category = categories[0]
	)

	fun Item.toCartItem(
		color: Long,
		size: Int,
		numberInCart: Int
	) = CartItem(
		id = id,
		name = name,
		photo = photos.last(),
		currentPrice = currentPrice,
		color = color,
		size = size,
		numberInCart = numberInCart
	)

}