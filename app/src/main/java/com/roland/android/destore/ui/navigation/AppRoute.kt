package com.roland.android.destore.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
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
	homeViewModel: HomeViewModel,
	detailsViewModel: DetailsViewModel,
	listViewModel: ListViewModel,
	cartViewModel: CartViewModel
) {
	NavHost(
		navController = navController,
		startDestination = AppRoute.HomeScreen.route
	) {
		animatedComposable(AppRoute.HomeScreen.route) {
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
			LaunchedEffect(true) {
				detailsViewModel.fetchDetails(itemId)
			}

			DetailsScreen(
				uiState = detailsViewModel.detailsUiState,
				actions = detailsViewModel::actions,
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
		animatedComposable(AppRoute.CartScreen.route) {
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
