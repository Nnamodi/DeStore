package com.roland.android.destore.ui.screen.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roland.android.destore.R
import com.roland.android.destore.ui.components.Colors
import com.roland.android.destore.ui.components.TopAppBar
import com.roland.android.destore.ui.components.VerticalGrid
import com.roland.android.destore.ui.navigation.Screens
import com.roland.android.destore.ui.screen.CommonScreen
import com.roland.android.destore.ui.screen.EmptyScreen
import com.roland.android.destore.ui.screen.LoadingScreen
import com.roland.android.destore.ui.screen.details.Sizes
import com.roland.android.destore.ui.screen.list.ListActions.Favorite
import com.roland.android.destore.ui.theme.DeStoreTheme
import com.roland.android.domain.data.State

@Composable
fun ListScreen(
	uiState: ListUiState,
	categoryName: String,
	isCategoryScreen: Boolean = true,
	isWishlistScreen: Boolean = false,
	actions: (ListActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val snackbarHostState = remember { SnackbarHostState() }

	Scaffold(
		topBar = {
			TopAppBar(
				title = categoryName,
				navigate = navigate,
				showBackButton = isCategoryScreen || isWishlistScreen
			)
	    },
		snackbarHost = { SnackbarHost(snackbarHostState) }
	) { paddingValues ->
		CommonScreen(
			state = if (isCategoryScreen) uiState.categorizedItems else uiState.allItems,
			loadingScreen = { errorMessage ->
				LoadingScreen(
					errorMessage = errorMessage,
					modifier = Modifier.fillMaxSize(),
					paddingValues = paddingValues,
					retry = { actions(ListActions.Reload) }
				)
			}
		) { items ->
			val list = if (isWishlistScreen) uiState.wishlistItems else items
			VerticalGrid(
				header = null,
				items = list,
				modifier = Modifier.padding(paddingValues),
				favoriteItems = uiState.wishlistItems,
				snackbarHostState = snackbarHostState,
				onFavorite = { actions(Favorite(it, !it.isFavorite(uiState.wishlistItems))) },
				addToCart = {
					actions(
						ListActions.AddToCart(
							item = it,
							color = Colors.entries.random().color.value.toLong(),
							size = Sizes.entries.random().value
						)
					)
				},
				onItemClick = { id, price ->
					navigate(Screens.DetailsScreen(id, price))
				}
			)

			if (list.isEmpty()) {
				EmptyScreen(
					displayText = stringResource(if (isWishlistScreen) R.string.nothing_in_wishlist else R.string.no_products),
					displayIcon = if (isWishlistScreen) Icons.Rounded.Favorite else Icons.Rounded.ShoppingBag,
					paddingValues = paddingValues
				)
			}
		}
	}
}

@Preview
@Composable
private fun ProductsScreenPreview() {
	val uiState = ListUiState(State.Error(Exception("No internet connection")))
	DeStoreTheme {
		ListScreen(
			uiState = uiState,
			categoryName = "",
			isCategoryScreen = false,
			isWishlistScreen = true,
			actions = {}
		) {}
	}
}