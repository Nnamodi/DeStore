package com.roland.android.destore.ui.screen.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roland.android.destore.R
import com.roland.android.destore.ui.components.CartItems
import com.roland.android.destore.ui.components.FixedBottomButton
import com.roland.android.destore.ui.components.TopAppBar
import com.roland.android.destore.ui.navigation.AppRoute
import com.roland.android.destore.ui.navigation.Screens
import com.roland.android.destore.ui.screen.EmptyScreen
import com.roland.android.destore.ui.theme.DeStoreTheme
import com.roland.android.destore.utils.Extensions.round
import com.roland.android.domain.data.CartItem

@Composable
fun CartScreen(
	uiState: CartUiState,
	actions: (CartActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val cartItems = uiState.cartItems

	Scaffold(
		topBar = {
			TopAppBar(
				title = stringResource(R.string.my_cart),
				navigate = navigate,
				showBackButton = false
			)
		}
	) { paddingValues ->
		if (uiState.cartItems.isNotEmpty()) {
			Box(contentAlignment = Alignment.BottomCenter) {
				CartItems(
					cartItems = cartItems,
					paddingValues = paddingValues,
					add = { actions(CartActions.Add(it))},
					remove = { actions(CartActions.Remove(it))},
					removeFromCart = { actions(CartActions.RemoveFromCart(it)) },
					viewDetails = { id, price ->
						navigate(Screens.DetailsScreen(id, price))
					}
				)
				FixedBottomButton(
					subTitle = stringResource(R.string.total_price),
					title = "€" + cartItems.sumOf { it.currentPrice.toDouble() }.round(),
					screen = AppRoute.CartScreen,
					buttonText = stringResource(R.string.checkout),
					navigate = navigate
				)
			}
		} else {
			EmptyScreen(paddingValues)
		}
	}
}

@Preview
@Composable
private fun CartScreenPreview() {
	DeStoreTheme {
		val uiState = CartUiState(listOf(
			CartItem(),
			CartItem(),
			CartItem(),
			CartItem()
		))
		CartScreen(uiState, {}) {}
	}
}