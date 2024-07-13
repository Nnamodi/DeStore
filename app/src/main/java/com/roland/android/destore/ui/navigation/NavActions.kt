package com.roland.android.destore.ui.navigation

import androidx.navigation.NavHostController

class NavActions(private val navController: NavHostController) {

	fun navigate(screen: Screens) {
		when (screen) {
			Screens.HomeScreen -> navigateToHomeScreen()
			is Screens.DetailsScreen -> navigateToDetailsScreen(screen.itemId)
			is Screens.ListScreen -> navigateToListScreen(screen.categoryId, screen.categoryName)
			Screens.SearchScreen -> {}
			Screens.CartScreen -> navigateToCartScreen()
			Screens.CheckoutScreen -> navigateToCheckoutScreen()
			Screens.Back -> navController.navigateUp()
		}
	}

	private fun navigateToHomeScreen() {
		navController.navigate(AppRoute.HomeScreen.route)
	}

	private fun navigateToDetailsScreen(itemId: String) {
		navController.navigate(
			AppRoute.DetailsScreen.routeWithInfo(itemId)
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

	private fun navigateToCartScreen() {
		navController.navigate(AppRoute.CartScreen.route)
	}

	private fun navigateToCheckoutScreen() {
		navController.navigate(AppRoute.CheckoutScreen.route)
	}

}

sealed class AppRoute(val route: String) {
	object HomeScreen: AppRoute("home_screen")
	object DetailsScreen: AppRoute("details_screen/{itemId}") {
		fun routeWithInfo(itemId: String) = String.format("details_screen/%s", itemId)
	}
	object ListScreen: AppRoute("list_screen/{category_id}/{category_name}") {
		fun routeWithCategory(
			categoryId: String,
			categoryName: String,
		) = String.format("list_screen/%s/%s", categoryId, categoryName)
	}
	object CartScreen: AppRoute("cart_screen")
	object CheckoutScreen: AppRoute("checkout_screen")
}

sealed class Screens {
	object HomeScreen : Screens()
	data class DetailsScreen(val itemId: String) : Screens()
	data class ListScreen(val categoryId: String, val categoryName: String) : Screens()
	object SearchScreen : Screens()
	object CartScreen : Screens()
	object CheckoutScreen : Screens()
	object Back : Screens()
}