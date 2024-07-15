package com.roland.android.destore.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.destore.R

@Composable
fun LoadingScreen(
	errorMessage: String?,
	paddingValues: PaddingValues,
	modifier: Modifier = Modifier,
	retry: () -> Unit
) {
	val isLoading = remember(errorMessage) { mutableStateOf(errorMessage == null) }

	Column(
		modifier = modifier
			.padding(paddingValues)
			.padding(30.dp)
			.verticalScroll(rememberScrollState()),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		if (isLoading.value) {
			CircularProgressIndicator()
		} else {
			Text("😔", Modifier.padding(vertical = 40.dp), fontSize = 130.sp)
			Text(
				text = errorMessage!!,
				fontSize = 24.sp,
				fontWeight = FontWeight.Medium
			)
			Button(
				onClick = retry,
				modifier = Modifier.padding(vertical = 30.dp),
				shape = RoundedCornerShape(8.dp)
			) {
				Text(text = stringResource(R.string.retry))
			}
		}
	}
}

@Composable
fun EmptyScreen(
	paddingValues: PaddingValues,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier
			.fillMaxSize()
			.padding(paddingValues)
			.padding(30.dp)
			.verticalScroll(rememberScrollState()),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Icon(
			imageVector = Icons.Rounded.ShoppingCart,
			contentDescription = stringResource(R.string.cart_is_empty),
			modifier = Modifier
				.padding(bottom = 20.dp)
				.size(100.dp)
				.rotate(-45f)
				.alpha(0.7f)
		)
		Text(
			text = stringResource(R.string.cart_is_empty),
			fontSize = 24.sp,
			fontStyle = FontStyle.Italic,
			fontWeight = FontWeight.Medium
		)
	}
}