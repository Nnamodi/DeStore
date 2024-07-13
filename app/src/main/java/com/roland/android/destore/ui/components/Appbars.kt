@file:OptIn(ExperimentalMaterial3Api::class)

package com.roland.android.destore.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.roland.android.destore.R
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
	navigate: (Screens) -> Unit
) {
	TopAppBar(
		title = {
			Text(
				text = title,
				fontWeight = FontWeight.Bold
			)
		},
		navigationIcon = {
			IconButton(onClick = { navigate(Screens.Back) }) {
				Icon(Icons.AutoMirrored.Rounded.ArrowBack, stringResource(R.string.back))
			}
		}
	)
}