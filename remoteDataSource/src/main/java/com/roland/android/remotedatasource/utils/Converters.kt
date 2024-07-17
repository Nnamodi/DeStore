package com.roland.android.remotedatasource.utils

import com.roland.android.remotedatasource.network.model.CategoryModel
import com.roland.android.remotedatasource.network.model.ImageModel
import com.roland.android.remotedatasource.network.model.ItemDetailsModel
import com.roland.android.remotedatasource.network.model.ItemModel
import com.roland.android.remotedatasource.network.model.ListModel
import com.roland.android.remotedatasource.network.model.PriceModel
import com.roland.android.remotedatasource.usecase.data.Category
import com.roland.android.remotedatasource.usecase.data.Image
import com.roland.android.remotedatasource.usecase.data.Item
import com.roland.android.remotedatasource.usecase.data.ItemDetails
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
		itemModel.price,
		itemModel.categories.map { convertToCategory(it) }.firstOrNull() ?: Category(name = "Generic")
	)

	fun convertToItemDetails(itemDetailsModel: ItemDetailsModel) = ItemDetails(
		itemDetailsModel.id,
		itemDetailsModel.name,
		itemDetailsModel.description,
		itemDetailsModel.photos.map { convertToImage(it) },
		itemDetailsModel.currentPrice,
		itemDetailsModel.categories.map { convertToCategory(it) }
			.takeIf { it.isNotEmpty() } ?: listOf(Category(name = "Generic"))
	)

	fun convertToList(listModel: ListModel) = List(
		listModel.items.map { convertToItem(it) },
		listModel.size
	)

	private fun convertToPrice(priceModel: PriceModel) = Price(
		priceModel.values
	)

	private fun convertToCategory(categoryModel: CategoryModel) = Category(
		id = categoryModel.id,
		name = categoryModel.name.capitalizeFirstLetter()
	)

	private fun String.capitalizeFirstLetter(): String {
		return substring(0, 1).uppercase() + substring(1)
	}

}