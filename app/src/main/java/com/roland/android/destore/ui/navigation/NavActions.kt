package com.roland.android.destore.ui.navigation

import android.annotation.SuppressLint
import androidx.navigation.NavHostController

class NavActions(private val navController: NavHostController) {

	fun navigate(screen: Screens, popBackStack: Boolean = false) {
		when (screen) {
			Screens.HomeScreen -> navigateToHomeScreen(popBackStack)
			is Screens.DetailsScreen -> navigateToDetailsScreen(screen.itemId, screen.itemPrice)
			is Screens.ListScreen -> navigateToListScreen(screen.categoryId, screen.categoryName)
			Screens.AllProductsScreen -> navigateToAllProductsScreen()
			Screens.CartScreen -> navigateToCartScreen()
			Screens.CheckoutScreen -> navigateToCheckoutScreen()
			Screens.OrderCompleteScreen -> navigateToOrderCompleteScreen()
			Screens.OrderHistoryScreen -> navigateToOrderHistoryScreen()
			is Screens.OrderDetailsScreen -> navigateToOrderDetailsScreen(screen.orderNo)
			is Screens.SignUpScreen -> navigateToSignUpScreen()
			Screens.Back -> navigateUp()
		}
	}

	private fun navigateToHomeScreen(popBackStack: Boolean = false) {
		navController.navigate(AppRoute.HomeScreen.route) {
			if (!popBackStack) return@navigate
			popUpTo(AppRoute.HomeScreen.route) {
				inclusive = false
			}
			launchSingleTop = true
		}
	}

	private fun navigateToDetailsScreen(
		itemId: String,
		itemPrice: String
	) {
		navController.navigate(
			AppRoute.DetailsScreen.routeWithInfo(itemId, itemPrice)
		)
	}

	private fun navigateToListScreen(
		categoryId: String,
		categoryName: String
	) {
		navController.navigate(
			AppRoute.ListScreen.routeWithCategory(categoryId, categoryName)
		)
	}

	private fun navigateToAllProductsScreen() {
		navController.navigate(AppRoute.AllProductsScreen.route)
	}

	private fun navigateToCartScreen() {
		navController.navigate(AppRoute.CartScreen.route)
	}

	private fun navigateToCheckoutScreen() {
		navController.navigate(AppRoute.CheckoutScreen.route)
	}

	private fun navigateToOrderCompleteScreen() {
		navController.navigate(AppRoute.OrderCompleteScreen.route)
	}

	private fun navigateToOrderHistoryScreen() {
		navController.navigate(AppRoute.OrderHistoryScreen.route)
	}

	private fun navigateToOrderDetailsScreen(orderNo: String) {
		navController.navigate(
			AppRoute.OrderDetailsScreen.routeWithId(orderNo)
		)
	}

	private fun navigateToSignUpScreen() {
		navController.navigate(AppRoute.SignUpScreen.route)
	}

	@SuppressLint("RestrictedApi")
	private fun navigateUp() {
		val navBackStackEntry = navController.currentBackStack.value
		val currentRoute = navBackStackEntry.last().destination.route
		if (currentRoute == AppRoute.OrderCompleteScreen.route) {
			navController.popBackStack(AppRoute.HomeScreen.route, false)
		} else {
			navController.navigateUp()
		}
	}

}

sealed class AppRoute(val route: String) {
	data object HomeScreen: AppRoute("home_screen")
	data object DetailsScreen: AppRoute("details_screen/{itemId}/{itemPrice}") {
		fun routeWithInfo(itemId: String, itemPrice: String) = String.format("details_screen/%s/%s", itemId, itemPrice)
	}
	data object ListScreen: AppRoute("list_screen/{category_id}/{category_name}") {
		fun routeWithCategory(
			categoryId: String,
			categoryName: String,
		) = String.format("list_screen/%s/%s", categoryId, categoryName)
	}
	data object AllProductsScreen: AppRoute("all_products_screen")
	data object CartScreen: AppRoute("cart_screen")
	data object CheckoutScreen: AppRoute("checkout_screen")
	data object OrderCompleteScreen: AppRoute("order_complete_screen")
	data object OrderHistoryScreen: AppRoute("order_history_screen")
	data object OrderDetailsScreen: AppRoute("order_details_screen/{order_no}") {
		fun routeWithId(orderNo: String) = String.format("order_details_screen/%s", orderNo)
	}
	data object SignUpScreen: AppRoute("sign_up_screen")
}

sealed class Screens {
	data object HomeScreen : Screens()
	data class DetailsScreen(val itemId: String, val itemPrice: String) : Screens()
	data object AllProductsScreen : Screens()
	data class ListScreen(val categoryId: String, val categoryName: String) : Screens()
	data object CartScreen : Screens()
	data object CheckoutScreen : Screens()
	data object OrderCompleteScreen : Screens()
	data object OrderHistoryScreen : Screens()
	data class OrderDetailsScreen(val orderNo: String) : Screens()
	data object SignUpScreen : Screens()
	data object Back : Screens()
}