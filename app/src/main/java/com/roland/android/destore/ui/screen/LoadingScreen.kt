package com.roland.android.destore.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.destore.R
import com.roland.android.destore.utils.shimmerModifier

@Composable
fun LoadingScreen(
	isLoading: Boolean,
	paddingValues: PaddingValues,
	modifier: Modifier = Modifier,
	retry: () -> Unit
) {
	Column(
		modifier = modifier
			.padding(paddingValues)
			.padding(vertical = 30.dp)
			.verticalScroll(rememberScrollState()),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		if (isLoading) {
			CircularProgressIndicator()
		} else {
			Text("😔", fontSize = 40.sp)
			Button(
				onClick = retry,
				modifier = Modifier.padding(vertical = 20.dp),
				shape = RoundedCornerShape(8.dp)
			) {
				Text(text = stringResource(R.string.retry))
			}
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