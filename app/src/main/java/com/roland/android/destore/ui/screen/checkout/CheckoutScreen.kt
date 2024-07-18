package com.roland.android.destore.ui.screen.checkout

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
import com.roland.android.destore.ui.screen.cart.CartActions
import com.roland.android.destore.ui.theme.DeStoreTheme
import com.roland.android.domain.data.CartItem

@Composable
fun CheckoutScreen(
	uiState: CheckoutUiState,
	actions: (CartActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val cartItems = uiState.cartItems

	Scaffold(
		topBar = { TopAppBar(stringResource(R.string.checkout), navigate) }
	) { paddingValues ->
		Box(contentAlignment = Alignment.BottomCenter) {
			CartItems(
				cartItems = cartItems,
				paddingValues = paddingValues,
				inCheckoutScreen = true,
				userInfo = uiState.userInfo,
				add = { actions(CartActions.Add(it))},
				remove = { actions(CartActions.Remove(it))},
				removeFromCart = { actions(CartActions.RemoveFromCart(it)) },
				backToCart = { navigate(Screens.Back) },
				viewDetails = { id, price ->
					navigate(Screens.DetailsScreen(id, price))
				}
			)
			FixedBottomButton(
				screen = AppRoute.CheckoutScreen,
				buttonText = stringResource(R.string.proceed),
				onButtonClick = { actions(CartActions.Checkout) },
				navigate = navigate
			)
		}
	}
}

@Preview
@Composable
private fun CheckoutScreenPreview() {
	DeStoreTheme {
		val uiState = CheckoutUiState(listOf(
			CartItem(),
			CartItem(),
			CartItem(),
			CartItem()
		))
		CheckoutScreen(uiState, {}) {}
	}
}