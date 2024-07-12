package com.roland.android.remotedatasource.utils

import com.roland.android.remotedatasource.network.model.ImageModel
import com.roland.android.remotedatasource.network.model.ItemModel
import com.roland.android.remotedatasource.network.model.ListModel
import com.roland.android.remotedatasource.network.model.PriceModel
import com.roland.android.remotedatasource.usecase.data.Image
import com.roland.android.remotedatasource.usecase.data.Item
import com.roland.android.remotedatasource.usecase.data.List
import com.roland.android.remotedatasource.usecase.data.Price

internal object Converters {

	private fun convertToImage(imageModel: ImageModel) = Image(
		imageModel.url
	)

	private fun convertToItem(itemModel: ItemModel) = Item(
		itemModel.id,
		itemModel.name,
		itemModel.photos.map { convertToImage(it) },
		itemModel.currentPrice.map { convertToPrice(it) }
			.firstOrNull()?.toString() ?: itemModel.price
	)

	fun convertToList(listModel: ListModel) = List(
		listModel.items.map { convertToItem(it) },
		listModel.size
	)

	private fun convertToPrice(priceModel: PriceModel) = Price(
		priceModel.values
	)

}