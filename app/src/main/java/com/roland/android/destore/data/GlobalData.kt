package com.roland.android.destore.data

import com.roland.android.domain.data.CartItem
import com.roland.android.domain.data.Item
import com.roland.android.domain.data.Order
import kotlinx.coroutines.flow.MutableStateFlow

val allItemsFlow: MutableStateFlow<List<Item>> = MutableStateFlow(emptyList())

val cartItemsFlow: MutableStateFlow<List<CartItem>> = MutableStateFlow(emptyList())

val wishlistItemsFlow: MutableStateFlow<List<Item>> = MutableStateFlow(emptyList())

val ordersFlow: MutableStateFlow<List<Order>> = MutableStateFlow(emptyList())
