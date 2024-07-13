package com.roland.android.destore.data

import com.roland.android.remotedatasource.usecase.data.CartItem
import com.roland.android.remotedatasource.usecase.data.Item
import kotlinx.coroutines.flow.MutableStateFlow

val cartItemsFlow: MutableStateFlow<List<CartItem>> = MutableStateFlow(emptyList())

val favoriteItemsFlow: MutableStateFlow<List<Item>> = MutableStateFlow(emptyList())