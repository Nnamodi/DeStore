package com.roland.android.destore.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.roland.android.destore.ui.components.NavBar
import com.roland.android.destore.ui.navigation.AppRoute
import com.roland.android.destore.ui.navigation.NavActions
import com.roland.android.destore.ui.screen.cart.CartViewModel
import com.roland.android.destore.ui.screen.details.DetailsViewModel
import com.roland.android.destore.ui.screen.home.HomeViewModel
import com.roland.android.destore.ui.screen.list.ListViewModel
import com.roland.android.destore.ui.theme.DeStoreTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			val navController = rememberNavController()
			val homeViewModel: HomeViewModel by inject()
			val detailsViewModel: DetailsViewModel by inject()
			val listViewModel: ListViewModel by inject()
			val cartViewModel: CartViewModel by inject()

			DeStoreTheme {
				Scaffold(bottomBar = { NavBar(navController) }) { paddingValues ->
					AppRoute(
						navActions = NavActions(navController),
						navController = navController,
						paddingValues = paddingValues,
						homeViewModel = homeViewModel,
						detailsViewModel = detailsViewModel,
						listViewModel = listViewModel,
						cartViewModel = cartViewModel
					)
				}
			}
		}
	}
}