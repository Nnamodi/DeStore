package com.roland.android.destore.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.roland.android.remotedatasource.utils.Constant.errorMessage
import com.roland.android.domain.data.State

@Composable
fun <T: Any>CommonScreen(
	state: State<T>?,
	paddingValues: PaddingValues = PaddingValues(0.dp),
	loadingScreen: @Composable (String?) -> Unit,
	successScreen: @Composable (T) -> Unit,
) {
	val layoutDirection = LocalLayoutDirection.current
	val context = LocalContext.current

	Box(
		Modifier.padding(
			start = paddingValues.calculateStartPadding(layoutDirection),
			end = paddingValues.calculateEndPadding(layoutDirection)
		)
	) {
		when (state) {
			null -> {
				loadingScreen(null)
			}
			is State.Error -> {
				loadingScreen(state.throwable.errorMessage(context))
			}
			is State.Success -> {
				successScreen(state.data)
			}
		}
	}
}