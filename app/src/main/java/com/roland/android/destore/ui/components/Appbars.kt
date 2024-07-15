@file:OptIn(ExperimentalMaterial3Api::class)

package com.roland.android.destore.ui.components

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CardTravel
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalMall
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.roland.android.destore.R
import com.roland.android.destore.ui.navigation.AppRoute
import com.roland.android.destore.ui.navigation.Screens

@Composable
fun HomeAppBar(navigate: (Screens) -> Unit) {
	TopAppBar(
		title = {
			Text(
				stringResource(R.string.app_name),
				fontWeight = FontWeight.Bold
			)
		},
		actions = {
			IconButton(onClick = { navigate(Screens.SearchScreen) }) {
				Icon(Icons.Rounded.Search, stringResource(R.string.search))
			}
		}
	)
}

@Composable
fun DetailsAppBar(navigate: (Screens) -> Unit) {
	TopAppBar(
		title = {},
		navigationIcon = {
			IconButton(
				onClick = { navigate(Screens.Back) },
				colors = IconButtonDefaults.iconButtonColors(Color.White, Color.Black)
			) {
				Icon(Icons.AutoMirrored.Rounded.ArrowBack, stringResource(R.string.back))
			}
		},
		colors = TopAppBarDefaults.topAppBarColors(Color.Transparent)
	)
}

@Composable
fun TopAppBar(
	title: String,
	navigate: (Screens) -> Unit,
	showBackButton: Boolean = true
) {
	TopAppBar(
		title = {
			Text(
				text = title,
				fontWeight = FontWeight.Bold
			)
		},
		navigationIcon = {
			if (showBackButton) {
				IconButton(onClick = { navigate(Screens.Back) }) {
					Icon(Icons.AutoMirrored.Rounded.ArrowBack, stringResource(R.string.back))
				}
			}
		}
	)
}

@SuppressLint("RestrictedApi")
@Composable
fun NavBar(
	navController: NavHostController,
	modifier: Modifier = Modifier
) {
	val navBackStackEntry = navController.currentBackStackEntryAsState()
	val currentRoute = navBackStackEntry.value?.destination?.route
	val backStack = navController.currentBackStack.collectAsState().value.map { it.destination.route }

	NavigationBar(modifier) {
		NavBarItems.entries.forEach { item ->
			NavigationBarItem(
				selected = when (item) {
					NavBarItems.Home -> item.route == currentRoute
					NavBarItems.AllProducts -> item.route == currentRoute || item.route in backStack
					NavBarItems.Cart -> item.route == currentRoute || item.route in backStack
				},
				onClick = {
					navController.navigate(item.route) {
						popUpTo(navController.graph.findStartDestination().id) {
							saveState = item.route != currentRoute
						}
						launchSingleTop = true
						restoreState = true
					}
				},
				icon = { Icon(item.icon, null) },
				label = { Text(stringResource(item.title)) }
			)
		}
	}
}

private enum class NavBarItems(
	@StringRes val title: Int,
	val icon: ImageVector,
	val route: String
) {
	Home(
		title = R.string.home,
		icon = Icons.Rounded.Home,
		route = AppRoute.HomeScreen.route
	),
	AllProducts(
		title = R.string.all_products,
		icon = Icons.Rounded.LocalMall,
		route = AppRoute.AllProductsScreen.route
	),
	Cart(
		title = R.string.cart,
		icon = Icons.Rounded.CardTravel,
		route = AppRoute.CartScreen.route
	)
}