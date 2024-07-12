@file:OptIn(ExperimentalFoundationApi::class)

package com.roland.android.destore.ui.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.destore.ui.components.HomeAppBar
import com.roland.android.destore.ui.components.LargePoster
import com.roland.android.destore.ui.components.VerticalGrid
import com.roland.android.destore.ui.screen.CommonScreen
import com.roland.android.destore.ui.screen.LoadingScreen
import com.roland.android.destore.ui.theme.Brown
import com.roland.android.destore.ui.theme.DeStoreTheme
import com.roland.android.destore.ui.theme.SkyBlue
import com.roland.android.destore.utils.Extensions.greetings
import com.roland.android.outlet.R
import com.roland.android.remotedatasource.usecase.data.Item
import com.roland.android.remotedatasource.utils.Constant.errorMessage
import com.roland.android.remotedatasource.utils.State
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun HomeScreen(
	uiState: HomeUiState,
	actions: (HomeActions) -> Unit,
	navigate: () -> Unit
) {
	val snackbarHostState = remember { SnackbarHostState() }
	val scope = rememberCoroutineScope()
	val context = LocalContext.current

	Scaffold(
		topBar = { HomeAppBar(navigate) },
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
			LazyColumn(contentPadding = paddingValues) {
				item {
					UserInfo(userName = uiState.userInfo)
				}

				item {
					val pagerState = rememberPagerState { 4 }

					Box(contentAlignment = Alignment.BottomCenter) {
						HorizontalPager(
							state = pagerState,
							beyondBoundsPageCount = 4,
						) { page ->
							PagerItems(
								index = page,
								item = data.featured[page],
								addToCart = {
									scope.launch {
										snackbarHostState.showSnackbar(
											context.getString(
												R.string.added_to_cart,
												it.name
											))
									}
									actions(HomeActions.AddToCart(it))
								},
								onItemClick = { navigate() }
							)
						}

						HorizontalPagerIndicator(
							pagerState = pagerState,
							modifier = Modifier.padding(bottom = 20.dp)
						)
					}
				}

				item {
					CategoryList()
				}

				item {
					VerticalGrid(
						header = stringResource(R.string.our_special_offers),
						items = data.specialOffers
					)
				}

				item {
					VerticalGrid(
						header = stringResource(R.string.featured),
						items = data.featured
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
			if (result == ActionPerformed) actions(HomeActions.Reload)
		}
	}
}

@Composable
private fun UserInfo(
	userName: String,
	modifier: Modifier = Modifier
) {
	val avatarText = userName.trim().split(" ").map { it.first() }.take(2)
	Row(modifier.padding(horizontal = 16.dp)) {
		Box(
			Modifier
				.clip(CircleShape)
				.background(Brown)
		) {
			Text(text = avatarText.joinToString().uppercase())
		}
		Column {
			Text(
				text = greetings(LocalContext.current),
				style = MaterialTheme.typography.labelLarge
			)
			Text(text = userName)
		}
	}
}

@Composable
private fun PagerItems(
	index: Int,
	item: Item,
	modifier: Modifier = Modifier,
	addToCart: (Item) -> Unit,
	onItemClick: (String) -> Unit
) {
	val backgroundColor = listOf(Color.Blue, Color.Red, Color.Green, Color.DarkGray)[index]

	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(16.dp)
			.height(200.dp)
			.background(backgroundColor)
			.clickable { onItemClick(item.id) }
	) {
		LargePoster(
			url = item.photos.last().url,
			contentDescription = item.name,
			modifier = Modifier.padding(start = 30.dp, top = 20.dp, bottom = 34.dp)
		)
		Column(
			modifier = Modifier
				.padding(horizontal = 20.dp)
				.weight(1f)
		) {
			Text(text = item.category.name, fontSize = 8.sp, color = Color.White)
			Text(text = "${item.name} €${item.name}", color = Color.White)
			Row(modifier = Modifier.fillMaxWidth()) {
				Spacer(Modifier.weight(1f))
				Button(
					onClick = { addToCart(item) },
					modifier = Modifier.padding(top = 20.dp),
					shape = RoundedCornerShape(6.dp),
					colors = ButtonDefaults.buttonColors(Color.White, backgroundColor)
				) {
					Icon(
						imageVector = Icons.Rounded.ShoppingBag,
						contentDescription = stringResource(R.string.add_to_cart)
					)
					Text(text = stringResource(R.string.add_to_cart))
				}
			}
		}
	}
}

@Composable
fun CategoryList(
	modifier: Modifier = Modifier
) {
}

@Composable
fun HorizontalPagerIndicator(
	pagerState: PagerState,
	modifier: Modifier = Modifier,
	indicatorColor: Color = SkyBlue,
	indicatorSize: Dp = 8.dp
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(indicatorSize),
		verticalAlignment = Alignment.CenterVertically
	) {
		repeat(pagerState.pageCount) { index ->
			val color = if (pagerState.currentPage == index || pagerState.targetPage == index) {
				val pageOffset = ((pagerState.currentPage - index) + pagerState.currentPageOffsetFraction).absoluteValue
				val offsetPercentage = 1f - pageOffset.coerceIn(0f, 1f)
				indicatorColor.copy(alpha = offsetPercentage)
			} else {
				indicatorColor.copy(alpha = 0.1f)
			}

			Box(
				modifier = Modifier
					.size(indicatorSize)
					.clip(MaterialTheme.shapes.small)
					.background(color)
			)
		}
	}
}

@Preview
@Composable
private fun HomeScreenPreview() {
	DeStoreTheme {
		val uiState = HomeUiState(State.Error(Exception("No internet connection")))
		HomeScreen(uiState, {}) {}
	}
}