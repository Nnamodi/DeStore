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
import com.roland.android.destore.ui.theme.DeStoreTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			val navController = rememberNavController()

			DeStoreTheme {
				Scaffold(bottomBar = { NavBar(navController) }) { paddingValues ->
					AppRoute(
						navActions = NavActions(navController),
						navController = navController,
						paddingValues = paddingValues
					)
				}
			}
		}
	}
}