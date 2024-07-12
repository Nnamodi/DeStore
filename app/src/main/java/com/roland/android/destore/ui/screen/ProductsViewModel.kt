package com.roland.android.destore.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.remotedatasource.usecase.GetProductsUseCase
import com.roland.android.remotedatasource.utils.State
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProductsViewModel : ViewModel(), KoinComponent {

	private val getProductsUseCase: GetProductsUseCase by inject()

	var data by mutableStateOf<State<GetProductsUseCase.Response>?>(null); private set

	init { fetchItems() }

	private fun fetchItems() {
		viewModelScope.launch {
			getProductsUseCase.execute(GetProductsUseCase.Request)
				.collect { data = it }
		}
	}

	fun retry() {
		data = null
		fetchItems()
	}

}