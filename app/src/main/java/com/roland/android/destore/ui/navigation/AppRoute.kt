package com.roland.android.destore.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.roland.android.destore.R
import com.roland.android.destore.ui.screen.cart.CartScreen
import com.roland.android.destore.ui.screen.cart.CartViewModel
import com.roland.android.destore.ui.screen.checkout.CheckoutScreen
import com.roland.android.destore.ui.screen.details.DetailsScreen
import com.roland.android.destore.ui.screen.details.DetailsViewModel
import com.roland.android.destore.ui.screen.home.HomeScreen
import com.roland.android.destore.ui.screen.home.HomeViewModel
import com.roland.android.destore.ui.screen.list.ListScreen
import com.roland.android.destore.ui.screen.list.ListViewModel
import com.roland.android.destore.utils.AnimationDirection
import com.roland.android.destore.utils.animatedComposable

@Composable
fun AppRoute(
	navActions: NavActions,
	navController: NavHostController,
	paddingValues: PaddingValues,
	homeViewModel: HomeViewModel,
	detailsViewModel: DetailsViewModel,
	listViewModel: ListViewModel,
	cartViewModel: CartViewModel
) {
	val layoutDirection = LocalLayoutDirection.current

	NavHost(
		navController = navController,
		startDestination = AppRoute.HomeScreen.route,
		modifier = Modifier.padding(
			PaddingValues(
				start = paddingValues.calculateStartPadding(layoutDirection),
				end = paddingValues.calculateEndPadding(layoutDirection),
				bottom = paddingValues.calculateBottomPadding()
			)
		)
	) {
		composable(AppRoute.HomeScreen.route) {
			HomeScreen(
				uiState = homeViewModel.homeUiState,
				actions = homeViewModel::actions,
				navigate = navActions::navigate
			)
		}
		animatedComposable(
			route = AppRoute.DetailsScreen.route,
			animationDirection = AnimationDirection.UpDown
		) { backStackEntry ->
			val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
			val itemPrice = backStackEntry.arguments?.getString("itemPrice") ?: ""
			LaunchedEffect(true) {
				detailsViewModel.fetchDetails(itemId)
			}

			DetailsScreen(
				uiState = detailsViewModel.detailsUiState,
				itemPrice = itemPrice,
				actions = detailsViewModel::actions,
				navigate = navActions::navigate
			)
		}
		composable(AppRoute.AllProductsScreen.route) {
			LaunchedEffect(true) { listViewModel.fetchData() }

			ListScreen(
				uiState = listViewModel.listUiState,
				categoryName = stringResource(R.string.all_products),
				isCategoryScreen = false,
				actions = listViewModel::actions,
				navigate = navActions::navigate
			)
		}
		animatedComposable(AppRoute.ListScreen.route) { backStackEntry ->
			val categoryId = backStackEntry.arguments?.getString("category_id") ?: ""
			val categoryName = backStackEntry.arguments?.getString("category_name") ?: ""
			LaunchedEffect(true) {
				listViewModel.fetchData(categoryId)
			}

			ListScreen(
				uiState = listViewModel.listUiState,
				categoryName = categoryName,
				actions = listViewModel::actions,
				navigate = navActions::navigate
			)
		}
		composable(AppRoute.CartScreen.route) {
			CartScreen(
				uiState = cartViewModel.cartUiState,
				actions = cartViewModel::actions,
				navigate = navActions::navigate
			)
		}
		animatedComposable(AppRoute.CheckoutScreen.route) {
			CheckoutScreen(
				uiState = cartViewModel.checkoutUiState,
				actions = cartViewModel::actions,
				navigate = navActions::navigate
			)
		}
	}
}
