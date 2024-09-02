package com.roland.android.destore.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.roland.android.destore.R
import com.roland.android.destore.ui.screen.cart.CartScreen
import com.roland.android.destore.ui.screen.cart.CartViewModel
import com.roland.android.destore.ui.screen.checkout.CheckoutScreen
import com.roland.android.destore.ui.screen.checkout.CheckoutViewModel
import com.roland.android.destore.ui.screen.checkout.OrderCompleteScreen
import com.roland.android.destore.ui.screen.details.DetailsScreen
import com.roland.android.destore.ui.screen.details.DetailsViewModel
import com.roland.android.destore.ui.screen.home.HomeScreen
import com.roland.android.destore.ui.screen.home.HomeViewModel
import com.roland.android.destore.ui.screen.list.ListScreen
import com.roland.android.destore.ui.screen.list.ListViewModel
import com.roland.android.destore.ui.screen.order_history.OrderDetailsScreen
import com.roland.android.destore.ui.screen.order_history.OrderHistoryScreen
import com.roland.android.destore.ui.screen.order_history.OrderHistoryViewModel
import com.roland.android.destore.utils.AnimationDirection
import com.roland.android.destore.utils.Extensions.WISHLIST
import com.roland.android.destore.utils.animatedComposable
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppRoute(
	navActions: NavActions,
	navController: NavHostController,
	paddingValues: PaddingValues,
	homeViewModel: HomeViewModel = koinViewModel(),
	detailsViewModel: DetailsViewModel = koinViewModel(),
	listViewModel: ListViewModel = koinViewModel(),
	cartViewModel: CartViewModel = koinViewModel(),
	checkoutViewModel: CheckoutViewModel = koinViewModel(),
	orderHistoryViewModel: OrderHistoryViewModel = koinViewModel()
) {
	NavHost(
		navController = navController,
		startDestination = AppRoute.HomeScreen.route
	) {
		composable(AppRoute.HomeScreen.route) {
			HomeScreen(
				uiState = homeViewModel.homeUiState,
				bottomPadding = paddingValues.calculateBottomPadding(),
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
			ListScreen(
				uiState = listViewModel.listUiState,
				categoryName = stringResource(R.string.all_products),
				isCategoryScreen = false,
				bottomPadding = paddingValues.calculateBottomPadding(),
				actions = listViewModel::actions,
				navigate = navActions::navigate
			)
		}
		animatedComposable(AppRoute.ListScreen.route) { backStackEntry ->
			val categoryId = backStackEntry.arguments?.getString("category_id") ?: ""
			val categoryName = backStackEntry.arguments?.getString("category_name") ?: ""
			LaunchedEffect(true) {
				if (categoryId == WISHLIST) return@LaunchedEffect
				listViewModel.fetchCategorizedData(categoryId)
			}

			ListScreen(
				uiState = listViewModel.listUiState,
				categoryName = categoryName,
				isCategoryScreen = categoryId != WISHLIST,
				isWishlistScreen = categoryId == WISHLIST,
				actions = listViewModel::actions,
				navigate = navActions::navigate
			)
		}
		composable(AppRoute.CartScreen.route) {
			CartScreen(
				uiState = cartViewModel.cartUiState,
				bottomPadding = paddingValues.calculateBottomPadding(),
				actions = cartViewModel::actions,
				navigate = navActions::navigate
			)
		}
		animatedComposable(AppRoute.CheckoutScreen.route) {
			CheckoutScreen(
				uiState = checkoutViewModel.checkoutUiState,
				actions = checkoutViewModel::actions,
				navigate = navActions::navigate
			)
		}
		animatedComposable(AppRoute.OrderCompleteScreen.route) {
			OrderCompleteScreen(navActions::navigate)
		}
		animatedComposable(AppRoute.OrderHistoryScreen.route) {
			OrderHistoryScreen(
				orders = orderHistoryViewModel.orders,
				navigate = navActions::navigate
			)
		}
		animatedComposable(AppRoute.OrderDetailsScreen.route) { backStackEntry ->
			val orderNo = backStackEntry.arguments?.getString("order_no") ?: ""
			LaunchedEffect(true) {
				orderHistoryViewModel.fetchOrderDetails(orderNo)
			}

			OrderDetailsScreen(
				orderDetails = orderHistoryViewModel.orderDetails,
				navigate = navActions::navigate
			)
		}
	}
}
