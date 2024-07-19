package com.roland.android.destore.ui.screen.order_history

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.ShoppingBasket
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roland.android.destore.R
import com.roland.android.destore.ui.components.TopAppBar
import com.roland.android.destore.ui.navigation.Screens
import com.roland.android.destore.ui.screen.EmptyScreen
import com.roland.android.destore.ui.theme.DeStoreTheme
import com.roland.android.destore.utils.Extensions.formatDate
import com.roland.android.domain.data.Order
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

typealias OrderNo = String

@Composable
fun OrderHistoryScreen(
	orders: List<Order>,
	navigate: (Screens) -> Unit
) {
	val layoutDirection = LocalLayoutDirection.current

	Scaffold(
		topBar = { TopAppBar(stringResource(R.string.order_history), navigate) }
	) { paddingValues ->
		LazyColumn(contentPadding = PaddingValues(
			start = paddingValues.calculateStartPadding(layoutDirection),
			top = paddingValues.calculateTopPadding(),
			end = paddingValues.calculateEndPadding(layoutDirection),
			bottom = paddingValues.calculateBottomPadding() + 100.dp
		)) {
			items(orders.size) { index ->
				val order = orders[index]

				OrderItem(order = order) {
					navigate(Screens.OrderDetailsScreen(it))
				}
			}
		}

		if (orders.isEmpty()) {
			EmptyScreen(
				displayText = stringResource(R.string.no_orders_placed),
				displayIcon = Icons.Rounded.ShoppingBasket,
				paddingValues = paddingValues
			)
		}
	}
}

@Composable
private fun OrderItem(
	order: Order,
	modifier: Modifier = Modifier,
	onClick: (OrderNo) -> Unit
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
			.clip(shapes.extraLarge)
			.border(
				width = 2.dp,
				color = colorScheme.onBackground.copy(alpha = 0.2f),
				shape = shapes.extraLarge
			)
			.clickable { onClick(order.orderNo) }
			.padding(16.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Column(Modifier.padding(end = 20.dp)) {
			Info(
				title = stringResource(R.string.order_number),
				detail = order.orderNo
			)
			Info(
				title = stringResource(R.string.order_date),
				detail = order.dateOfPurchase.formatDate()
			)
			Info(
				title = stringResource(R.string.order_cost),
				detail = "€" + order.amount
			)
		}
		Spacer(Modifier.weight(1f))
		Icon(
			imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
			contentDescription = stringResource(R.string.view_order_details)
		)
	}
}

@Composable
fun Info(
	title: String,
	detail: String
) {
	Row(Modifier.padding(bottom = 10.dp)) {
		Text(title, fontWeight = FontWeight.Medium)
		Text(detail, Modifier.padding(start = 6.dp))
	}
}

@Preview
@Composable
private fun OrderHistoryScreenPreview() {
	DeStoreTheme {
		OrderHistoryScreen(emptyList()) {}
	}
}