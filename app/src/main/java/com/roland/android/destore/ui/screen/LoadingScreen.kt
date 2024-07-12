package com.roland.android.destore.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roland.android.destore.ui.components.VerticalGrid
import com.roland.android.destore.ui.theme.DeStoreTheme
import com.roland.android.destore.utils.shimmerModifier

@Composable
fun LoadingScreen(
	isLoading: Boolean,
	paddingValues: PaddingValues,
	modifier: Modifier = Modifier
) {
	VerticalGrid("", paddingValues) {
		items(10) {
			BoxItem(isLoading)
		}
	}
}

@Composable
private fun BoxItem(
	isLoading: Boolean,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier
			.padding(end = 10.dp, bottom = 10.dp)
			.clip(MaterialTheme.shapes.large)
			.background(Color.LightGray.copy(alpha = 0.2f))
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 20.dp)
				.height(150.dp)
				.then(Modifier.shimmerModifier(isLoading))
		)
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(start = 10.dp, end = 40.dp, bottom = 10.dp)
				.height(16.dp)
				.then(Modifier.shimmerModifier(isLoading))
		)
		Box(
			modifier = Modifier
				.padding(start = 10.dp, bottom = 20.dp)
				.size(30.dp, 14.dp)
				.alpha(0.7f)
				.then(Modifier.shimmerModifier(isLoading))
		)
	}
}

@Preview
@Composable
private fun LoadingScreenPreview() {
	DeStoreTheme {
		LoadingScreen(isLoading = true, paddingValues = PaddingValues())
	}
}