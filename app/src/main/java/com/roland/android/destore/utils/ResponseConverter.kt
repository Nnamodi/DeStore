package com.roland.android.destore.utils

import com.roland.android.destore.ui.screen.home.HomeDataModel
import com.roland.android.destore.ui.screen.home.HomeUiState
import com.roland.android.remotedatasource.usecase.GetCategoryUseCase
import com.roland.android.remotedatasource.usecase.GetProductUseCase
import com.roland.android.remotedatasource.usecase.GetProductsUseCase
import com.roland.android.remotedatasource.usecase.data.Item
import com.roland.android.remotedatasource.usecase.data.ItemDetails
import com.roland.android.remotedatasource.utils.State

object ResponseConverter {

	fun convertToHomeData(response: State<GetProductsUseCase.Response>): State<HomeDataModel> {
		return when (response) {
			is State.Error -> {
				State.Error(response.throwable)
			}
			is State.Success -> {
				State.Success(
					HomeDataModel(
						featured = response.data.featured,
						specialOffers = response.data.specialOffers
					)
				)
			}
		}
	}

	fun convertToDetailsData(response: State<GetProductUseCase.Response>): State<ItemDetails> {
		return when (response) {
			is State.Error -> {
				State.Error(response.throwable)
			}
			is State.Success -> {
				State.Success(response.data.details)
			}
		}
	}

	fun convertToCategoryData(response: State<GetCategoryUseCase.Response>): State<List<Item>> {
		return when (response) {
			is State.Error -> {
				State.Error(response.throwable)
			}
			is State.Success -> {
				State.Success(response.data.data)
			}
		}
	}

}