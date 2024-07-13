package com.roland.android.destore.ui.screen.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roland.android.destore.R
import com.roland.android.destore.ui.components.Colors
import com.roland.android.destore.ui.components.TopAppBar
import com.roland.android.destore.ui.components.VerticalGrid
import com.roland.android.destore.ui.navigation.Screens
import com.roland.android.destore.ui.screen.CommonScreen
import com.roland.android.destore.ui.screen.LoadingScreen
import com.roland.android.destore.ui.screen.details.DetailsActions
import com.roland.android.destore.ui.screen.details.Sizes
import com.roland.android.destore.ui.screen.list.ListActions.AddToCart
import com.roland.android.destore.ui.screen.list.ListActions.Favorite
import com.roland.android.destore.ui.theme.DeStoreTheme
import com.roland.android.remotedatasource.utils.State

@Composable
fun ListScreen(
	uiState: ListUiState,
	categoryName: String,
	actions: (ListActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val snackbarHostState = remember { SnackbarHostState() }

	Scaffold(
		topBar = { TopAppBar(categoryName, navigate) },
		snackbarHost = { SnackbarHost(snackbarHostState) }
	) { paddingValues ->
		CommonScreen(
			state = uiState.data,
			loadingScreen = {
				LoadingScreen(
					isLoading = uiState.data == null,
					modifier = Modifier.fillMaxSize(),
					paddingValues = paddingValues,
					retry = { actions(ListActions.Reload) }
				)
			}
		) { items ->
			VerticalGrid(
				header = stringResource(R.string.featured),
				items = items,
				favoriteItems = uiState.favoriteItems,
				snackbarHostState = snackbarHostState,
				onFavorite = { actions(Favorite(it, !it.isFavorite(uiState.favoriteItems))) },
				addToCart = {
					actions(
						ListActions.AddToCart(
							item = it,
							color = Colors.entries.random().color.value.toLong(),
							size = Sizes.entries.random().value
						)
					)
				},
				onItemClick = { navigate(Screens.DetailsScreen(it)) }
			)
		}
	}
}

@Preview
@Composable
private fun ProductsScreenPreview() {
	val uiState = ListUiState(State.Error(Exception("No internet connection")))
	DeStoreTheme {
		ListScreen(uiState, "", {}) {}
	}
}