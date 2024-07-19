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
import com.roland.android.destore.ui.theme.DeStoreTheme
import com.roland.android.domain.data.CartItem

@Composable
fun CheckoutScreen(
	uiState: CheckoutUiState,
	actions: (CheckoutActions) -> Unit,
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
				screen = AppRoute.CheckoutScreen,
				userInfo = uiState.userInfo,
				backToCart = { navigate(Screens.Back) },
				viewDetails = { id, price ->
					navigate(Screens.DetailsScreen(id, price))
				}
			)
			FixedBottomButton(
				screen = AppRoute.CheckoutScreen,
				buttonText = stringResource(R.string.proceed),
				onButtonClick = { actions(CheckoutActions.Checkout) },
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