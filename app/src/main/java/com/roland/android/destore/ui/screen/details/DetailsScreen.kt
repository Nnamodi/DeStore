@file:OptIn(ExperimentalFoundationApi::class)

package com.roland.android.destore.ui.screen.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.destore.ui.components.DetailsAppBar
import com.roland.android.destore.ui.components.TopAppBar
import com.roland.android.destore.ui.components.LargePoster
import com.roland.android.destore.ui.components.VerticalGrid
import com.roland.android.destore.ui.screen.CommonScreen
import com.roland.android.destore.ui.screen.LoadingScreen
import com.roland.android.destore.ui.screen.home.HorizontalPagerIndicator
import com.roland.android.destore.ui.theme.DeStoreTheme
import com.roland.android.outlet.R
import com.roland.android.remotedatasource.usecase.data.Item
import com.roland.android.remotedatasource.usecase.data.ItemDetails
import com.roland.android.remotedatasource.utils.Constant.errorMessage
import com.roland.android.remotedatasource.utils.State
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun DetailsScreen(
	uiState: DetailsUiState,
	actions: (DetailsActions) -> Unit,
	navigate: () -> Unit
) {
	val snackbarHostState = remember { SnackbarHostState() }
	val scope = rememberCoroutineScope()
	val context = LocalContext.current
	val layoutDirection = LocalLayoutDirection.current

	Scaffold(
		topBar = { DetailsAppBar(navigate) },
		snackbarHost = { SnackbarHost(snackbarHostState) }
	) { paddingValues ->
		CommonScreen(
			state = uiState.data,
			loadingScreen = { errorMessage ->
				LoadingScreen(
					isLoading = errorMessage == null,
					paddingValues = paddingValues
				)
			}
		) { data ->
			LazyColumn(contentPadding = PaddingValues(
				start = paddingValues.calculateStartPadding(layoutDirection),
				end = paddingValues.calculateEndPadding(layoutDirection),
				bottom = paddingValues.calculateBottomPadding()
			)) {
				item {
					PhotoPager(item = data.details)
				}

				item {
				}

				item {
				}

				item {
					VerticalGrid(
						header = stringResource(
							R.string.more_from,
							data.details.categories.random().name
						),
						items = data.moreInStore
					)
				}
			}
		}
	}

	// shows error snackbar
	LaunchedEffect(uiState.data) {
		if (uiState.data !is State.Error) return@LaunchedEffect
		scope.launch {
			val result = snackbarHostState.showSnackbar(
				message = uiState.data.throwable.errorMessage(context),
				actionLabel = context.getString(R.string.retry)
			)
			if (result == ActionPerformed) actions(DetailsActions.Reload)
		}
	}
}

@Composable
private fun PhotoPager(
	item: ItemDetails,
	modifier: Modifier = Modifier
) {
	val pagerState = rememberPagerState { 4 }

	Box(
		modifier = modifier.fillMaxHeight(0.45f),
		contentAlignment = Alignment.BottomCenter
	) {
		HorizontalPager(
			state = pagerState,
			beyondBoundsPageCount = 4,
		) { page ->
			LargePoster(
				url = item.photos[page].url,
				contentDescription = null,
				modifier = Modifier.fillMaxSize()
			)
		}

		HorizontalPagerIndicator(
			pagerState = pagerState,
			modifier = Modifier.padding(bottom = 20.dp)
		)
	}
}

@Preview
@Composable
private fun DetailsScreenPreview() {
	DeStoreTheme {
		val uiState = DetailsUiState(State.Error(Exception("No internet connection")))
		DetailsScreen(uiState, {}) {}
	}
}