package com.roland.android.remotedatasource.usecase.data

import kotlin.collections.List

data class List(
	val items: List<Item> = emptyList(),
	val size: Int = 20
)
