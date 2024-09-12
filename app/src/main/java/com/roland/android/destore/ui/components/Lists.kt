package com.roland.android.destore.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachEmail
import androidx.compose.material.icons.rounded.LocalConvenienceStore
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.PermContactCalendar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.destore.R
import com.roland.android.destore.ui.navigation.AppRoute
import com.roland.android.destore.ui.screen.order_history.Info
import com.roland.android.destore.ui.theme.SkyBlue
import com.roland.android.destore.utils.Extensions.filterCartItems
import com.roland.android.destore.utils.Extensions.round
import com.roland.android.destore.utils.Extensions.transformList
import com.roland.android.domain.data.CartItem
import com.roland.android.domain.data.Item
import com.roland.android.domain.data.UserInfo
import kotlinx.coroutines.launch

@Composable
fun VerticalGrid(
	header: String?,
	items: List<Item>,
	modifier: Modifier = Modifier,
	favoriteItems: List<Item>,
	showOriginalPrice: Boolean = false,
	snackbarHostState: SnackbarHostState,
	bottomPadding: Dp = 0.dp,
	onFavorite: (Item) -> Unit,
	addToCart: (Item) -> Unit,
	onItemClick: (ItemId, ItemPrice) -> Unit
) {
	val scope = rememberCoroutineScope()
	val context = LocalContext.current

	Column {
		header?.let {
			Text(
				text = it,
				modifier = modifier.padding(start = 10.dp),
				style = MaterialTheme.typography.headlineMedium
			)
		}
		LazyVerticalGrid(
			columns = GridCells.Adaptive(150.dp),
			modifier = modifier.padding(start = 10.dp),
			contentPadding = PaddingValues(bottom = bottomPadding + 100.dp)
		) {
			items(items.size) { index ->
				val item = items[index]
				Product(
					item = item,
					favoriteItems = favoriteItems,
					favorite = {
						scope.launch {
							snackbarHostState.showSnackbar(
								context.getString(
									if (item.isFavorite(favoriteItems)) R.string.removed_from_wishlist else R.string.added_to_wishlist,
									it.name
								)
							)
						}
						onFavorite(it)
					},
					showOriginalPrice = showOriginalPrice,
					addToCart = {
						scope.launch {
							snackbarHostState.showSnackbar(
								context.getString(
									R.string.added_to_cart,
									it.name
								)
							)
						}
						addToCart(it)
					},
					onItemClick = onItemClick
				)
			}
		}
	}
}

@Composable
fun GroupedVerticalGrid(
	header: String,
	gridItems: List<Item>,
	modifier: Modifier = Modifier,
	favoriteItems: List<Item>,
	showOriginalPrice: Boolean = false,
	numberOfRows: Int,
	snackbarHostState: SnackbarHostState,
	onFavorite: (Item) -> Unit,
	addToCart: (Item) -> Unit,
	onItemClick: (ItemId, ItemPrice) -> Unit
) {
	val scope = rememberCoroutineScope()
	val context = LocalContext.current
	val items = gridItems.take(numberOfRows * 2).transformList()

	Column(modifier.padding(start = 10.dp)) {
		Text(
			text = header,
			modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 6.dp),
			fontWeight = FontWeight.Bold,
			style = MaterialTheme.typography.headlineSmall
		)
		repeat(items.size) { index ->
			val item = items[index]
			val even = index % 2 == 0
			val twoItemsInARow = even || item.getOrNull(1) != null

			Row(Modifier.fillMaxWidth()) {
				Product(
					item = item[0],
					modifier = if (twoItemsInARow) Modifier.weight(1f) else Modifier.fillMaxWidth(0.5f),
					favoriteItems = favoriteItems,
					favorite = {
						scope.launch {
							snackbarHostState.showSnackbar(
								context.getString(
									if (item[0].isFavorite(favoriteItems)) R.string.removed_from_wishlist else R.string.added_to_wishlist,
									it.name
								)
							)
						}
						onFavorite(it)
				    },
					showOriginalPrice = showOriginalPrice,
					addToCart = {
						scope.launch {
							snackbarHostState.showSnackbar(
								context.getString(
									R.string.added_to_cart,
									it.name
								)
							)
						}
						addToCart(it)
					},
					onItemClick = onItemClick
				)
				if (twoItemsInARow) {
					Product(
						item = item[1],
						modifier = Modifier.weight(1f),
						favoriteItems = favoriteItems,
						favorite = {
							scope.launch {
								snackbarHostState.showSnackbar(
									context.getString(
										if (item[1].isFavorite(favoriteItems)) R.string.removed_from_wishlist else R.string.added_to_wishlist,
										it.name
									)
								)
							}
							onFavorite(it)
						},
						showOriginalPrice = showOriginalPrice,
						addToCart = {
							scope.launch {
								snackbarHostState.showSnackbar(
									context.getString(
										R.string.added_to_cart,
										it.name
									)
								)
							}
							addToCart(it)
						},
						onItemClick = onItemClick
					)
				}
			}
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CartItems(
	cartItems: List<CartItem>,
	modifier: Modifier = Modifier,
	paddingValues: PaddingValues,
	screen: AppRoute,
	dateOfPurchase: String = "",
	purchaseAmount: String = "",
	userInfo: UserInfo = UserInfo(),
	add: (CartItem) -> Unit = {},
	remove: (CartItem) -> Unit = {},
	removeFromCart: (CartItem) -> Unit = {},
	backToCart: () -> Unit = {},
	viewDetails: (ItemId, ItemPrice) -> Unit = { _, _ ->},
	navigateToEditUserInfo: () -> Unit = {},
	navigateToEditAddress: () -> Unit = {}
) {
	val layoutDirection = LocalLayoutDirection.current
	val items = cartItems.distinctBy { it.id } // will refactor this line for better experience on cart screen.
	val inCheckoutScreen = screen is AppRoute.CheckoutScreen

	LazyColumn(
		modifier = modifier.fillMaxSize(),
		contentPadding = PaddingValues(
			start = paddingValues.calculateStartPadding(layoutDirection),
			top = paddingValues.calculateTopPadding(),
			end = paddingValues.calculateEndPadding(layoutDirection),
			bottom = 100.dp
		)
	) {
		item {
			if (screen is AppRoute.OrderDetailsScreen) {
				Column(Modifier.padding(16.dp)) {
					Info(
						title = stringResource(R.string.order_date),
						detail = dateOfPurchase
					)
					Info(
						title = stringResource(R.string.order_cost),
						detail = purchaseAmount
					)
				}
			}
		}
		item {
			if (inCheckoutScreen) {
				SmallHeader(
					header = stringResource(R.string.order_list),
					modifier = Modifier.padding(16.dp),
					onButtonClick = backToCart
				)
			}
		}
		items(items.size) { index ->
			val item = items[index]

			CartItem(
				item = item,
				modifier = Modifier.animateItemPlacement(),
				screen = screen,
				numberInCart = cartItems.filterCartItems(item).size,
				add = add,
				remove = remove,
				removeFromCart = removeFromCart,
				viewDetails = viewDetails
			)
		}
		if (inCheckoutScreen) {
			item {
				Container(
					header = stringResource(R.string.personal_information),
					navigate = navigateToEditUserInfo
				) {
					Column {
						Row(Modifier.fillMaxWidth()) {
							Label(
								label = userInfo.name.withPlaceholder,
								icon = Icons.Rounded.PermContactCalendar
							)
							Spacer(Modifier.weight(1f))
							Label(
								label = userInfo.phone.withPlaceholder,
								icon = Icons.Rounded.Numbers
							)
						}
						Label(
							label = userInfo.email.withPlaceholder,
							icon = Icons.Rounded.AttachEmail
						)
					}
				}
			}
			item {
				Container(
					header = stringResource(R.string.delivery_option),
					navigate = navigateToEditAddress
				) {
					Row(Modifier.fillMaxWidth()) {
						Label(label = stringResource(R.string.pick_up_point), icon = Icons.Rounded.LocalConvenienceStore)
						Spacer(Modifier.weight(1f))
						Label(label = userInfo.address.withPlaceholder)
					}
				}
			}
			item {
				OrderDetails(cartItems)
			}
		}
	}
}

@Composable
private fun SmallHeader(
	header: String,
	modifier: Modifier = Modifier,
	showButton: Boolean = true,
	onButtonClick: () -> Unit = {}
) {
	Row(modifier.fillMaxWidth()) {
		Text(text = header, fontSize = 15.sp, fontWeight = FontWeight.Bold)
		Spacer(Modifier.weight(1f))
		if (showButton) {
			Text(
				text = stringResource(R.string.edit),
				modifier = Modifier.clickable { onButtonClick() },
				color = SkyBlue,
				fontSize = 15.sp,
				fontWeight = FontWeight.Bold
			)
		}
	}
}

@Composable
fun Container(
	header: String,
	modifier: Modifier = Modifier,
	showNavigateButton: Boolean = true,
	backgroundColor: Color = SkyBlue.copy(alpha = 0.2f),
	navigate: () -> Unit = {},
	content: @Composable ColumnScope.() -> Unit
) {
	Column(
		Modifier
			.fillMaxWidth()
			.padding(16.dp)
	) {
		SmallHeader(
			header = header,
			modifier = Modifier.padding(bottom = 4.dp),
			showButton = showNavigateButton,
			onButtonClick = navigate
		)
		Column(
			modifier = modifier
				.fillMaxWidth()
				.clip(shapes.large)
				.background(backgroundColor)
				.padding(12.dp)
		) {
			content()
		}
	}
}

@Composable
private fun OrderDetails(
	cartItems: List<CartItem>,
	modifier: Modifier = Modifier
) {
	val subTotal = cartItems.sumOf { it.currentPrice.toDouble() }.round()
	val deliveryFee = ((5 / 100.0) * subTotal).round()
	val discount = ((3 / 100.0) * subTotal).round()
	val total = (subTotal + deliveryFee - discount).toDouble().round()

	Container(
		header = stringResource(R.string.price_summary),
		modifier = modifier,
		showNavigateButton = false,
		navigate = {}
	) {
		Row {
			Column(Modifier.weight(1f)) {
				Text(stringResource(R.string.total_price), Modifier.alpha(0.7f))
				Text(stringResource(R.string.delivery_fee), Modifier.alpha(0.7f))
				Text(stringResource(R.string.discount), Modifier.alpha(0.7f))
			}
			Column {
				repeat(3) { Text("€") }
			}
			Column(horizontalAlignment = Alignment.End) {
				Text("$subTotal", Modifier.padding(start = 10.dp), fontWeight = FontWeight.Medium)
				Text("$deliveryFee", fontWeight = FontWeight.Medium)
				Text("$discount", fontWeight = FontWeight.Medium)
			}
		}
		HorizontalDivider(
			Modifier
				.padding(vertical = 20.dp)
				.background(colorScheme.background)
		)
		Row(Modifier.fillMaxWidth()) {
			Text(
				text = stringResource(R.string.total),
				modifier = Modifier
					.alpha(0.7f)
					.weight(1f)
			)
			Text("€", Modifier.padding(end = 10.dp))
			Text("$total", fontWeight = FontWeight.Medium)
		}
	}
}

@Composable
private fun Label(
	label: String,
	modifier: Modifier = Modifier,
	icon: ImageVector? = null
) {
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically
	) {
		icon?.let {
			Icon(
				imageVector = icon,
				contentDescription = label,
				modifier = Modifier
					.padding(end = 6.dp)
					.scale(0.7f),
				tint = colorScheme.primary
			)
		}
		Text(text = label, fontSize = 13.sp)
	}
}

private val String.withPlaceholder get() = takeIf { it.isNotEmpty() } ?: "___"
