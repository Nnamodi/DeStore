package com.roland.android.destore.ui.screen.checkout

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.destore.R
import com.roland.android.destore.ui.components.TopAppBar
import com.roland.android.destore.ui.navigation.Screens
import com.roland.android.destore.ui.theme.DarkGreen

@Composable
fun OrderCompleteScreen(navigate: (Screens) -> Unit) {
	Scaffold(
		topBar = { TopAppBar(stringResource(R.string.order_complete), navigate) }
	) { paddingValues ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
				.padding(30.dp),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Icon(
				painter = painterResource(R.drawable.successful),
				contentDescription = null,
				modifier = Modifier
					.padding(bottom = 4.dp)
					.size(100.dp),
				tint = DarkGreen
			)
			Text(
				text = stringResource(R.string.payment_successful),
				fontSize = 24.sp,
				fontWeight = FontWeight.Medium
			)
			Text(
				text = stringResource(R.string.order_complete_message),
				modifier = Modifier.padding(top = 6.dp, bottom = 20.dp),
				textAlign = TextAlign.Center
			)
			Button(
				onClick = { navigate(Screens.Back) },
				modifier = Modifier.fillMaxWidth(),
				shape = RoundedCornerShape(8.dp)
			) {
				Text(text = stringResource(R.string.okay))
			}
		}
	}

	BackHandler { navigate(Screens.Back) }
}