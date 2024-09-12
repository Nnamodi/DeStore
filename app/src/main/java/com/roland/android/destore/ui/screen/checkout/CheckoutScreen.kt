package com.roland.android.destore.ui.screen.checkout

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.roland.android.domain.data.UserInfo

@Composable
fun CheckoutScreen(
	uiState: CheckoutUiState,
	actions: (CheckoutActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val openEditUserInfoSheet = rememberSaveable { mutableStateOf(false) }
	val openEditAddressSheet = rememberSaveable { mutableStateOf(false) }
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
				},
				navigateToEditUserInfo = { openEditUserInfoSheet.value = true },
				navigateToEditAddress = { openEditAddressSheet.value = true }
			)
			FixedBottomButton(
				screen = AppRoute.CheckoutScreen,
				buttonText = stringResource(R.string.proceed),
				enabled = uiState.userInfo.isSet(),
				onButtonClick = { actions(CheckoutActions.Checkout) },
				navigate = navigate
			)
		}

		if (openEditUserInfoSheet.value) {
			InputUserInfoSheet(
				userInfo = uiState.userInfo,
				onInfoChange = { actions(CheckoutActions.UpdateUserInfo(it)) },
				onDismiss = { openEditUserInfoSheet.value = false }
			)
		}

		if (openEditAddressSheet.value) {
			InputAddressSheet(
				address = uiState.userInfo.address,
				onAddressChange = { newAddress ->
					val newUserInfo = UserInfo(
						name = uiState.userInfo.name,
						email = uiState.userInfo.email,
						phone = uiState.userInfo.phone,
						address = newAddress
					)
					actions(CheckoutActions.UpdateUserInfo(newUserInfo))
				},
				onDismiss = { openEditAddressSheet.value = false }
			)
		}
	}
}

private fun UserInfo.isSet(): Boolean {
	return name.isNotEmpty() && email.isNotEmpty()
			&& phone.isNotEmpty() && address.isNotEmpty()
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