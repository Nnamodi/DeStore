@file:OptIn(ExperimentalFoundationApi::class)

package com.roland.android.destore.ui.screen.details

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.destore.R
import com.roland.android.destore.ui.components.ColorBox
import com.roland.android.destore.ui.components.Colors
import com.roland.android.destore.ui.components.Container
import com.roland.android.destore.ui.components.DetailsAppBar
import com.roland.android.destore.ui.components.FavoriteIconButton
import com.roland.android.destore.ui.components.LargePoster
import com.roland.android.destore.ui.components.QuantityButton
import com.roland.android.destore.ui.components.VerticalGrid
import com.roland.android.destore.ui.navigation.Screens
import com.roland.android.destore.ui.screen.CommonScreen
import com.roland.android.destore.ui.screen.LoadingScreen
import com.roland.android.destore.ui.screen.details.DetailsActions.AddToCart
import com.roland.android.destore.ui.screen.details.DetailsActions.Favorite
import com.roland.android.destore.ui.screen.details.DetailsActions.RemoveFromCart
import com.roland.android.destore.ui.screen.home.HorizontalPagerIndicator
import com.roland.android.destore.ui.theme.Black
import com.roland.android.destore.ui.theme.DeStoreTheme
import com.roland.android.destore.ui.theme.Grey
import com.roland.android.destore.ui.theme.SkyBlue
import com.roland.android.destore.utils.Extensions.toItem
import com.roland.android.remotedatasource.usecase.data.ItemDetails
import com.roland.android.remotedatasource.utils.State

@Composable
fun DetailsScreen(
	uiState: DetailsUiState,
	actions: (DetailsActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val snackbarHostState = remember { SnackbarHostState() }
	val layoutDirection = LocalLayoutDirection.current

	Scaffold(
		topBar = { DetailsAppBar(navigate) },
		snackbarHost = { SnackbarHost(snackbarHostState) }
	) { paddingValues ->
		CommonScreen(
			state = uiState.details,
			loadingScreen = { errorMessage ->
				LoadingScreen(
					isLoading = errorMessage == null,
					modifier = Modifier.fillMaxSize(),
					paddingValues = paddingValues,
					retry = { actions(DetailsActions.Reload) }
				)
			}
		) { details ->
			LazyColumn(contentPadding = PaddingValues(
				start = paddingValues.calculateStartPadding(layoutDirection),
				end = paddingValues.calculateEndPadding(layoutDirection),
				bottom = paddingValues.calculateBottomPadding()
			)) {
				item {
					PhotoPager(item = details)
				}

				item {
					Description(
						item = details,
						isFavorite = uiState.favorited,
						onFavorite = {
							actions(Favorite(it.toItem(), !uiState.favorited))
						}
					)
				}

				item {
					Selection(
						numberInCart = uiState.numberInCart,
						addToCart = { colorValue, size ->
							actions(AddToCart(details.toItem(), colorValue, size))
						},
						removeFromCart = {
							actions(RemoveFromCart(details.toItem()))
						}
					)
				}

				item {
					CommonScreen(
						state = uiState.moreInStore,
						loadingScreen = {
							LoadingScreen(
								isLoading = it == null,
								paddingValues = paddingValues,
								retry = { actions(DetailsActions.ReloadCategoryList) }
							)
						}
					) { moreInStore ->
						VerticalGrid(
							header = stringResource(R.string.more_from, details.categories[0].name),
							items = moreInStore,
							favoriteItems = uiState.favoriteItems,
							snackbarHostState = snackbarHostState,
							onFavorite = { actions(Favorite(it, !uiState.favorited)) },
							addToCart = {
								actions(
									AddToCart(
										item = it,
										color = Colors.entries.random().color.value.toLong(),
										size = Sizes.entries.random().value
									)
								)
							},
							onItemClick = { navigate(Screens.DetailsScreen(it)) }
						)
					}
				}
			}
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

@Composable
private fun Description(
	item: ItemDetails,
	isFavorite: Boolean,
	modifier: Modifier = Modifier,
	onFavorite: (ItemDetails) -> Unit
) {
	Column(
		modifier
			.fillMaxWidth()
			.padding(16.dp)
	) {
		Text(
			text = item.categories[0].name,
			modifier = Modifier.padding(horizontal = 10.dp),
			fontSize = 8.sp
		)
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = item.name,
				modifier = Modifier.padding(vertical = 10.dp),
				fontWeight = FontWeight.Bold,
				style = MaterialTheme.typography.titleLarge
			)
			Spacer(Modifier.weight(1f))
			FavoriteIconButton(
				itemIsFavorite = isFavorite,
				favorite = { onFavorite(item) }
			)
		}
		Text(
			text = "€" + item.currentPrice,
			fontWeight = FontWeight.Medium
		)

		Text(
			text = stringResource(R.string.description),
			modifier = Modifier.padding(top = 16.dp),
			fontWeight = FontWeight.Medium
		)
		Text(
			text = item.description,
			modifier = Modifier.padding(bottom = 16.dp)
		)
	}
}

@Composable
private fun Selection(
	modifier: Modifier = Modifier,
	numberInCart: Int,
	addToCart: (Long, Int) -> Unit,
	removeFromCart: () -> Unit,
) {
	var selectedSize by rememberSaveable { mutableStateOf(Sizes.entries.random()) }
	var selectedColor by rememberSaveable { mutableStateOf(Colors.entries.random()) }

	Column(modifier.padding(horizontal = 16.dp)) {
		Container(
			header = stringResource(R.string.size),
			modifier = Modifier.padding(bottom = 16.dp),
			showNavigateButton = false
		) {
			Row(
				modifier = Modifier.horizontalScroll(rememberScrollState()),
				horizontalArrangement = Arrangement.spacedBy(12.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Sizes.entries.forEach { size ->
					SizeBox(
						size = size,
						selected = size == selectedSize,
						onSelect = { selectedSize = it }
					)
				}
			}
		}
		Container(
			header = stringResource(R.string.colors),
			modifier = Modifier.padding(bottom = 16.dp),
			showNavigateButton = false
		) {
			Row(
				modifier = Modifier.horizontalScroll(rememberScrollState()),
				horizontalArrangement = Arrangement.spacedBy(12.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Colors.entries.forEach { color ->
					ColorBox(
						color = color,
						selected = color == selectedColor,
						onSelect = { selectedColor = it }
					)
				}
			}
		}
		Container(
			header = stringResource(R.string.quantity),
			modifier = Modifier.padding(bottom = 46.dp),
			showNavigateButton = false
		) {
			QuantityButton(
				itemSize = numberInCart.toString(),
				inCheckoutScreen = false,
				add = {
					addToCart(
						selectedColor.color.value.toLong(),
						selectedSize.value
					)
				},
				remove = removeFromCart
			)
		}
	}
}

@Composable
private fun SizeBox(
	size: Sizes,
	selected: Boolean,
	modifier: Modifier = Modifier,
	onSelect: (Sizes) -> Unit = {}
) {
	Box(
		modifier
			.animateContentSize()
			.size(24.dp)
			.background(if (selected) SkyBlue else Grey)
			.padding(12.dp, 4.dp)
			.clip(MaterialTheme.shapes.small)
			.clickable { onSelect(size) }
	) {
		Text(
			text = "${size.value}",
			modifier = Modifier.padding(),
			color = if (selected) White else Black,
			fontSize = 15.sp
		)
	}
}

enum class Sizes(@StringRes val value: Int) {
	ThirtyTwo(32),
	ThirtyFive(35),
	ThirtyEight(38),
	Forty(40),
	FortyTwo(42),
	FortyFive(45)
}

@Preview
@Composable
private fun DetailsScreenPreview() {
	DeStoreTheme {
		val uiState = DetailsUiState(State.Error(Exception("No internet connection")))
		DetailsScreen(uiState, {}) {}
	}
}