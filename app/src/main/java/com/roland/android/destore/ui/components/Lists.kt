package com.roland.android.destore.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachEmail
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.LocalConvenienceStore
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.PermContactCalendar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.destore.R
import com.roland.android.destore.data.UserInfo
import com.roland.android.destore.ui.theme.SkyBlue
import com.roland.android.destore.utils.Extensions.getColor
import com.roland.android.destore.utils.Extensions.round
import com.roland.android.remotedatasource.usecase.data.CartItem
import com.roland.android.remotedatasource.usecase.data.Item
import kotlinx.coroutines.launch

@Composable
fun VerticalGrid(
	header: String?,
	items: List<Item>,
	modifier: Modifier = Modifier,
	favoriteItems: List<Item>,
	showOriginalPrice: Boolean = false,
	snackbarHostState: SnackbarHostState,
	onFavorite: (Item) -> Unit,
	addToCart: (Item) -> Unit,
	onItemClick: (String, String) -> Unit
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
			modifier = modifier.padding(start = 10.dp)
		) {
			items(items.size) { index ->
				Product(
					item = items[index],
					favoriteItems = favoriteItems,
					favorite = {
						scope.launch {
							snackbarHostState.showSnackbar(
								context.getString(
									R.string.added_to_favorite,
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
	onItemClick: (String, String) -> Unit
) {
	val scope = rememberCoroutineScope()
	val context = LocalContext.current
	val items = gridItems.take(numberOfRows * 2)

	Column(modifier.padding(start = 10.dp)) {
		Text(
			text = header,
			modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 6.dp),
			fontWeight = FontWeight.Bold,
			style = MaterialTheme.typography.headlineSmall
		)
		repeat(numberOfRows) { index ->
			Row(Modifier.fillMaxWidth()) {
				Product(
					item = items[index],
					modifier = Modifier.weight(1f),
					favoriteItems = favoriteItems,
					favorite = {
						scope.launch {
							snackbarHostState.showSnackbar(
								context.getString(
									R.string.added_to_favorite,
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
				Product(
					item = items[index + 1],
					modifier = Modifier.weight(1f),
					favoriteItems = favoriteItems,
					favorite = {
						scope.launch {
							snackbarHostState.showSnackbar(
								context.getString(
									R.string.added_to_favorite,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CartItems(
	cartItems: List<CartItem>,
	modifier: Modifier = Modifier,
	paddingValues: PaddingValues,
	inCheckoutScreen: Boolean = false,
	userInfo: UserInfo = UserInfo(),
	add: (CartItem) -> Unit = {},
	remove: (CartItem) -> Unit = {},
	removeFromCart: (CartItem) -> Unit = {},
	backToCart: () -> Unit = {},
	viewDetails: (String, String) -> Unit = { _, _ ->}
) {
	val layoutDirection = LocalLayoutDirection.current
	val items = cartItems.toSet().toList()

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
				inCheckoutScreen = inCheckoutScreen,
				numberInCart = cartItems.filter { it.id == item.id }.size,
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
					navigate = {}
				) {
					Column {
						Row(Modifier.fillMaxWidth()) {
							Label(label = userInfo.name, icon = Icons.Rounded.PermContactCalendar)
							Spacer(Modifier.weight(1f))
							Label(label = userInfo.name, icon = Icons.Rounded.Numbers)
						}
						Label(label = userInfo.email, icon = Icons.Rounded.AttachEmail)
					}
				}
			}
			item {
				Container(
					header = stringResource(R.string.delivery_option),
					navigate = {}
				) {
					Row(Modifier.fillMaxWidth()) {
						Label(label = stringResource(R.string.pick_up_point), icon = Icons.Rounded.LocalConvenienceStore)
						Spacer(Modifier.weight(1f))
						Label(label = userInfo.address)
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
private fun CartItem(
	item: CartItem,
	modifier: Modifier = Modifier,
	inCheckoutScreen: Boolean,
	numberInCart: Int,
	add: (CartItem) -> Unit,
	remove: (CartItem) -> Unit,
	removeFromCart: (CartItem) -> Unit,
	viewDetails: (String, String) -> Unit
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
			.clip(shapes.extraLarge)
			.border(
				width = 2.dp,
				color = colorScheme.onBackground.copy(alpha = 0.2f),
				shape = shapes.extraLarge
			)
			.clickable(!inCheckoutScreen) { viewDetails(item.id, item.currentPrice) }
			.padding(16.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		SmallPoster(
			url = item.photo.url,
			contentDescription = item.name,
			modifier = Modifier.clip(shapes.extraLarge)
		)
		Column(Modifier.padding(start = 10.dp)) {
			Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
				Text(text = item.name)
				Spacer(Modifier.weight(1f))
				if (!inCheckoutScreen) {
					IconButton(onClick = { removeFromCart(item) }) {
						Icon(
							Icons.Rounded.Clear,
							stringResource(R.string.remove_from_cart),
							Modifier.scale(0.8f)
						)
					}
				}
			}
			Row(verticalAlignment = Alignment.CenterVertically) {
				val colorValue = item.color.getColor()

				ColorBox(colorValue, Modifier.padding(top = 4.dp, bottom = 6.dp))
				Text(
					text = stringResource(colorValue.nameRes),
					modifier = Modifier.alpha(0.9f),
					fontSize = 12.sp
				)
				Box(
					modifier = Modifier
						.padding(horizontal = 6.dp, vertical = 2.dp)
						.size(2.dp, 8.dp)
						.background(colorScheme.onBackground.copy(alpha = 0.5f))
				)
				Text(stringResource(R.string.size))
				Text(
					text = item.size.toString(),
					modifier = Modifier
						.padding(start = 2.dp)
						.clip(shape = shapes.extraSmall)
						.background(colorScheme.onBackground.copy(alpha = 0.1f))
						.padding(4.dp, 2.dp)
				)
			}
			Row(verticalAlignment = Alignment.CenterVertically) {
				QuantityButton(
					itemSize = numberInCart.toString(),
					inCheckoutScreen = inCheckoutScreen,
					add = { add(item) },
					remove = { remove(item) }
				)
				if (inCheckoutScreen) {
					Box(
						modifier = Modifier
							.padding(horizontal = 6.dp, vertical = 2.dp)
							.size(2.dp, 8.dp)
							.background(colorScheme.onBackground.copy(alpha = 0.5f))
					)
				}
				Text("€" + item.currentPrice, Modifier.padding(start = 4.dp))
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
	backgroundColor: Color = SkyBlue.copy(alpha = 0.3f),
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
				Text("$subTotal", Modifier.padding(start = 10.dp))
				Text("$deliveryFee")
				Text("$discount")
			}
		}
		HorizontalDivider(Modifier.padding(vertical = 20.dp))
		Row(Modifier.fillMaxWidth()) {
			Text(
				text = stringResource(R.string.total),
				modifier = Modifier
					.alpha(0.7f)
					.weight(1f)
			)
			Text("€", Modifier.padding(end = 10.dp))
			Text("$total")
		}
	}
}

@Composable
fun QuantityButton(
	itemSize: String,
	inCheckoutScreen: Boolean,
	add: () -> Unit,
	remove: () -> Unit
) {
	Row(
		modifier = Modifier
			.clip(shape = shapes.medium)
			.background(colorScheme.onBackground.copy(alpha = 0.1f))
			.padding(horizontal = if (inCheckoutScreen) 0.dp else 6.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		if (!inCheckoutScreen) {
			Text(
				text = "-",
				modifier = Modifier
					.clickable { remove() }
					.padding(horizontal = 4.dp),
				fontSize = 16.sp
			)
		}
		Text(
			text = itemSize,
			modifier = Modifier
				.padding(if (inCheckoutScreen) 0.dp else 4.dp)
				.clip(shape = shapes.medium)
				.background(colorScheme.onBackground.copy(alpha = 0.1f))
				.padding(8.dp, 4.dp)
		)
		if (!inCheckoutScreen) {
			Text(
				text = "+",
				modifier = Modifier
					.clickable { add() }
					.padding(horizontal = 4.dp),
				fontSize = 16.sp
			)
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
