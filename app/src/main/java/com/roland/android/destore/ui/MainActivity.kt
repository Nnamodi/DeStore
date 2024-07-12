package com.roland.android.destore.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.roland.android.destore.ui.screen.ProductsScreen
import com.roland.android.destore.ui.screen.ProductsViewModel
import com.roland.android.destore.ui.theme.DeStoreTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			val viewModel: ProductsViewModel by inject()

			DeStoreTheme {
				ProductsScreen(
					data = viewModel.data,
					retry = viewModel::retry
				)
			}
		}
	}
}