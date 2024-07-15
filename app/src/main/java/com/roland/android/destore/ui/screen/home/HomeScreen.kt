@file:OptIn(ExperimentalFoundationApi::class)

package com.roland.android.destore.ui.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.destore.R
import com.roland.android.destore.ui.components.GroupedVerticalGrid
import com.roland.android.destore.ui.components.HomeAppBar
import com.roland.android.destore.ui.components.SmallPoster
import com.roland.android.destore.ui.navigation.Screens
import com.roland.android.destore.ui.screen.CommonScreen
import com.roland.android.destore.ui.screen.LoadingScreen
import com.roland.android.destore.ui.theme.Black
import com.roland.android.destore.ui.theme.Brown
import com.roland.android.destore.ui.theme.DeStoreTheme
import com.roland.android.destore.ui.theme.Purple
import com.roland.android.destore.ui.theme.Red
import com.roland.android.destore.ui.theme.SkyBlue
import com.roland.android.destore.utils.Extensions.greetings
import com.roland.android.remotedatasource.usecase.data.Item
import com.roland.android.remotedatasource.utils.State
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun HomeScreen(
	uiState: HomeUiState,
	actions: (HomeActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val snackbarHostState = remember { SnackbarHostState() }
	val scope = rememberCoroutineScope()
	val context = LocalContext.current
	val layoutDirection = LocalLayoutDirection.current

	Scaffold(
		topBar = { HomeAppBar(navigate) },
		snackbarHost = { SnackbarHost(snackbarHostState) }
	) { paddingValues ->
		CommonScreen(
			state = uiState.data,
			loadingScreen = { errorMessage ->
				LoadingScreen(
					errorMessage = errorMessage,
					modifier = Modifier.fillMaxSize(),
					paddingValues = paddingValues,
					retry = { actions(HomeActions.Reload) }
				)
			}
		) { data ->
			LazyColumn(contentPadding = PaddingValues(
				start = paddingValues.calculateStartPadding(layoutDirection),
				top = paddingValues.calculateTopPadding(),
				end = paddingValues.calculateEndPadding(layoutDirection),
				bottom = paddingValues.calculateBottomPadding() + 50.dp
			)) {
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
											)
										)
									}
									actions(HomeActions.AddToCart(it))
								},
								onItemClick = { id, price ->
									navigate(Screens.DetailsScreen(id, price))
								}
							)
						}

						HorizontalPagerIndicator(
							pagerState = pagerState,
							modifier = Modifier.padding(bottom = 30.dp)
						)
					}
				}

				item {
					CategoryList { id, name ->
						navigate(Screens.ListScreen(id, name))
					}
				}

				item {
					GroupedVerticalGrid(
						header = stringResource(R.string.our_special_offers),
						gridItems = data.specialOffers,
						favoriteItems = uiState.favoriteItems,
						showOriginalPrice = true,
						numberOfRows = 2,
						snackbarHostState = snackbarHostState,
						onFavorite = { actions(HomeActions.Favorite(it, !it.isFavorite(uiState.favoriteItems))) },
						addToCart = { actions(HomeActions.AddToCart(it)) },
						onItemClick = { id, price ->
							navigate(Screens.DetailsScreen(id, price))
						}
					)
				}

				item {
					GroupedVerticalGrid(
						header = stringResource(R.string.featured),
						gridItems = data.featured,
						favoriteItems = uiState.favoriteItems,
						numberOfRows = 3,
						snackbarHostState = snackbarHostState,
						onFavorite = { actions(HomeActions.Favorite(it, !it.isFavorite(uiState.favoriteItems))) },
						addToCart = { actions(HomeActions.AddToCart(it)) },
						onItemClick = { id, price ->
							navigate(Screens.DetailsScreen(id, price))
						}
					)
				}
			}
		}
	}
}

@Composable
private fun UserInfo(
	userName: String,
	modifier: Modifier = Modifier
) {
	val avatarText = userName.trim().split(" ").map { it.first() }.take(2).joinToString()
	Row(
		modifier = modifier.padding(start = 16.dp, bottom = 10.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Box(
			Modifier
				.clip(CircleShape)
				.background(Brown)
		) {
			Text(
				text = avatarText.replace(",", "").uppercase(),
				modifier = Modifier.padding(10.dp),
				color = Black,
				fontWeight = FontWeight.SemiBold,
				letterSpacing = 2.sp
			)
		}
		Column(Modifier.padding(start = 4.dp)) {
			Text(
				text = LocalContext.current.greetings(),
				fontWeight = FontWeight.Normal,
				style = MaterialTheme.typography.labelLarge
			)
			Text(text = userName, style = MaterialTheme.typography.titleMedium)
		}
	}
}

@Composable
private fun PagerItems(
	index: Int,
	item: Item,
	modifier: Modifier = Modifier,
	addToCart: (Item) -> Unit,
	onItemClick: (String, String) -> Unit
) {
	val backgroundColor = listOf(SkyBlue, Brown, Purple, Red)[index]

	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(16.dp)
			.height(200.dp)
			.clip(MaterialTheme.shapes.large)
			.background(backgroundColor)
			.clickable { onItemClick(item.id, item.currentPrice) },
		verticalAlignment = Alignment.CenterVertically
	) {
		SmallPoster(
			url = item.photos.last().url,
			contentDescription = item.name,
			modifier = Modifier
				.padding(start = 20.dp, top = 20.dp, bottom = 34.dp)
				.fillMaxWidth(0.4f)
		)
		Column(
			modifier = Modifier
				.padding(horizontal = 20.dp)
				.weight(1f),
			horizontalAlignment = Alignment.End
		) {
			Column {
				Text(
					text = item.category.name,
					fontSize = 10.sp,
					color = Color.White
				)
				Text(text = "${item.name} €${item.currentPrice}", color = Color.White)
			}
			Button(
				onClick = { addToCart(item) },
				modifier = Modifier.padding(top = 10.dp),
				shape = RoundedCornerShape(6.dp),
				colors = ButtonDefaults.buttonColors(Color.White, backgroundColor)
			) {
				Icon(
					imageVector = Icons.Rounded.ShoppingBag,
					contentDescription = stringResource(R.string.add_to_cart),
					modifier = Modifier
						.padding(end = 4.dp)
						.alpha(0.8f)
				)
				Text(text = stringResource(R.string.add_to_cart))
			}
		}
	}
}

@Composable
fun CategoryList(
	modifier: Modifier = Modifier,
	navigate: (String, String) -> Unit
) {
}

@Composable
fun HorizontalPagerIndicator(
	pagerState: PagerState,
	modifier: Modifier = Modifier,
	indicatorColor: Color = Color.White,
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
				indicatorColor.copy(alpha = 0.5f)
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