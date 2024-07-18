package com.roland.android.domain.data

import kotlin.collections.List

data class List(
	val items: List<Item> = emptyList(),
	val size: Int = 20
)
