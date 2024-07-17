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
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.destore.R
import com.roland.android.destore.ui.components.ColorBox
import com.roland.android.destore.ui.components.Colors
import com.roland.android.destore.ui.components.DetailsAppBar
import com.roland.android.destore.ui.components.FavoriteIconButton
import com.roland.android.destore.ui.components.FixedBottomButton
import com.roland.android.destore.ui.components.GroupedVerticalGrid
import com.roland.android.destore.ui.components.LargePoster
import com.roland.android.destore.ui.components.QuantityButton
import com.roland.android.destore.ui.navigation.AppRoute
import com.roland.android.destore.ui.navigation.Screens
import com.roland.android.destore.ui.screen.CommonScreen
import com.roland.android.destore.ui.screen.LoadingScreen
import com.roland.android.destore.ui.screen.details.DetailsActions.AddToCart
import com.roland.android.destore.ui.screen.details.DetailsActions.Favorite
import com.roland.android.destore.ui.screen.details.DetailsActions.RemoveFromCart
import com.roland.android.destore.ui.screen.home.HorizontalPagerIndicator
import com.roland.android.destore.ui.theme.Black
import com.roland.android.destore.ui.theme.Brown
import com.roland.android.destore.ui.theme.DeStoreTheme
import com.roland.android.destore.ui.theme.Grey
import com.roland.android.destore.ui.theme.SkyBlue
import com.roland.android.destore.utils.Extensions.toItem
import com.roland.android.remotedatasource.usecase.data.ItemDetails
import com.roland.android.remotedatasource.utils.State
import kotlinx.coroutines.launch
import kotlin.math.min

typealias ColorValue = Long
typealias Size = Int

@Composable
fun DetailsScreen(
	uiState: DetailsUiState,
	itemPrice: String,
	actions: (DetailsActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val snackbarHostState = remember { SnackbarHostState() }
	val layoutDirection = LocalLayoutDirection.current
	val context = LocalContext.current
	val scope = rememberCoroutineScope()

	Scaffold(
		topBar = { DetailsAppBar(navigate) },
		snackbarHost = { SnackbarHost(snackbarHostState) }
	) { paddingValues ->
		CommonScreen(
			state = uiState.details,
			loadingScreen = { errorMessage ->
				LoadingScreen(
					errorMessage = errorMessage,
					modifier = Modifier.fillMaxSize(),
					paddingValues = paddingValues,
					retry = { actions(DetailsActions.Reload) }
				)
			}
		) { details ->
			val addToCart: () -> Unit = {
				actions(
					AddToCart(
						item = details.toItem(itemPrice),
						color = Colors.entries.random().color.value.toLong(),
						size = Sizes.entries.random().value
					)
				)
			}

			Box(contentAlignment = Alignment.BottomCenter) {
				LazyColumn(contentPadding = PaddingValues(
					start = paddingValues.calculateStartPadding(layoutDirection),
					end = paddingValues.calculateEndPadding(layoutDirection),
					bottom = paddingValues.calculateBottomPadding() + 100.dp
				)) {
					item {
						PhotoPager(item = details)
					}

					item {
						Description(
							item = details,
							itemPrice = itemPrice,
							isFavorite = uiState.favorited,
							onFavorite = {
								scope.launch {
									snackbarHostState.showSnackbar(
										context.getString(
											R.string.added_to_favorite,
											it.name
										)
									)
								}
								actions(Favorite(it.toItem(itemPrice), !uiState.favorited))
							}
						)
					}

					item {
						Selection(
							numberInCart = uiState.numberInCart,
							addToCart = { colorValue, size ->
								actions(AddToCart(details.toItem(itemPrice), colorValue, size))
							},
							removeFromCart = { colorValue, size ->
								actions(RemoveFromCart(details.toItem(itemPrice), colorValue, size))
							}
						)
					}

					item {
						CommonScreen(
							state = uiState.moreInStore,
							loadingScreen = { errorMessage ->
								LoadingScreen(
									errorMessage = errorMessage,
									paddingValues = paddingValues,
									retry = { actions(DetailsActions.ReloadCategoryList) }
								)
							}
						) { moreInStore ->
							GroupedVerticalGrid(
								header = stringResource(R.string.more_from, details.categories[0].name),
								gridItems = moreInStore,
								favoriteItems = uiState.favoriteItems,
								numberOfRows = min(2, moreInStore.size),
								snackbarHostState = snackbarHostState,
								onFavorite = { actions(Favorite(it, !uiState.favorited)) },
								addToCart = { addToCart() },
								onItemClick = { id, price ->
									navigate(Screens.DetailsScreen(id, price))
								}
							)
						}
					}
				}
				FixedBottomButton(
					subTitle = stringResource(R.string.total_price),
					title = "€$itemPrice",
					screen = AppRoute.DetailsScreen,
					buttonText = stringResource(R.string.add_to_cart),
					onButtonClick = {
						scope.launch {
							snackbarHostState.showSnackbar(
								context.getString(
									R.string.added_to_cart,
									details.name
								)
							)
						}
						addToCart()
					}
				)
			}
		}
	}
}

@Composable
private fun PhotoPager(
	item: ItemDetails,
	modifier: Modifier = Modifier
) {
	val pagerState = rememberPagerState { item.photos.size }

	Box(
		modifier = modifier.height(300.dp),
		contentAlignment = Alignment.BottomCenter
	) {
		HorizontalPager(
			state = pagerState,
			beyondBoundsPageCount = item.photos.size,
		) { page ->
			LargePoster(
				url = item.photos.reversed()[page].url,
				contentDescription = null,
				modifier = Modifier.fillMaxSize()
			)
		}

		HorizontalPagerIndicator(
			pagerState = pagerState,
			modifier = Modifier
				.padding(bottom = 14.dp)
				.clip(MaterialTheme.shapes.small)
				.background(Brown.copy(alpha = 0.5f))
				.padding(6.dp)
		)
	}
}

@Composable
private fun Description(
	item: ItemDetails,
	itemPrice: String,
	isFavorite: Boolean,
	modifier: Modifier = Modifier,
	onFavorite: (ItemDetails) -> Unit
) {
	Column(
		modifier
			.fillMaxWidth()
			.padding(16.dp)
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			Column(Modifier.weight(1f)) {
				Text(item.categories[0].name, style = MaterialTheme.typography.bodySmall)
				Text(
					text = item.name,
					modifier = Modifier.padding(vertical = 4.dp),
					fontWeight = FontWeight.Bold,
					lineHeight = 22.sp,
					style = MaterialTheme.typography.titleLarge
				)
				Text(
					text = "€$itemPrice",
					fontSize = 15.sp,
					fontWeight = FontWeight.Medium
				)
			}
			FavoriteIconButton(
				itemIsFavorite = isFavorite,
				favorite = { onFavorite(item) }
			)
		}

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
	addToCart: (ColorValue, Size) -> Unit,
	removeFromCart: (ColorValue, Size) -> Unit,
) {
	var selectedSize by rememberSaveable { mutableStateOf(Sizes.entries.random()) }
	var selectedColor by rememberSaveable { mutableStateOf(Colors.entries.random()) }

	Column(modifier.padding(horizontal = 16.dp)) {
		SelectionBox(
			header = stringResource(R.string.size),
			modifier = Modifier.padding(bottom = 8.dp)
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
		SelectionBox(
			header = stringResource(R.string.colors),
			modifier = Modifier.padding(bottom = 8.dp)
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
		SelectionBox(
			header = stringResource(R.string.quantity),
			modifier = Modifier.padding(bottom = 46.dp)
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
				remove = {
					removeFromCart(
						selectedColor.color.value.toLong(),
						selectedSize.value
					)
				}
			)
		}
	}
}

@Composable
private fun SelectionBox(
	header: String,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) {
	Column(modifier.fillMaxWidth()) {
		Text(
			text = header,
			fontSize = 15.sp,
			fontWeight = FontWeight.Bold
		)
		content()
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
			.clip(MaterialTheme.shapes.small)
			.background(if (selected) SkyBlue else Grey)
			.clickable { onSelect(size) }
			.padding(12.dp, 4.dp)
	) {
		Text(
			text = "${size.value}",
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
		DetailsScreen(uiState, "", {}) {}
	}
}