package com.roland.android.destore.ui.screen.order_history

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roland.android.destore.R
import com.roland.android.destore.ui.components.CartItems
import com.roland.android.destore.ui.components.TopAppBar
import com.roland.android.destore.ui.navigation.AppRoute
import com.roland.android.destore.ui.navigation.Screens
import com.roland.android.destore.ui.theme.DeStoreTheme
import com.roland.android.destore.utils.Extensions.formatDate
import com.roland.android.domain.data.OrderDetails

@Composable
fun OrderDetailsScreen(
	orderDetails: OrderDetails,
	navigate: (Screens) -> Unit
) {
	val cartItems = orderDetails.orderItems

	Scaffold(
		topBar = {
			TopAppBar(
				title = stringResource(R.string.order_details),
				navigate = navigate,
				subTitle = stringResource(R.string.order_no, orderDetails.orderNo),
			)
		}
	) { paddingValues ->
		CartItems(
			cartItems = cartItems,
			paddingValues = paddingValues,
			screen = AppRoute.OrderDetailsScreen,
			dateOfPurchase = orderDetails.dateOfPurchase.formatDate(),
			purchaseAmount = "€" + orderDetails.amount,
			viewDetails = { id, price ->
				navigate(Screens.DetailsScreen(id, price))
			}
		)
	}
}

@Preview
@Composable
private fun OrderDetailsScreenPreview() {
	DeStoreTheme {
		OrderDetailsScreen(OrderDetails()) {}
	}
}