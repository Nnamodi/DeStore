package com.roland.android.destore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.destore.R
import com.roland.android.destore.ui.theme.SkyBlue
import com.roland.android.destore.utils.Extensions.getColor
import com.roland.android.remotedatasource.usecase.data.CartItem
import com.roland.android.remotedatasource.usecase.data.Item
import com.roland.android.destore.data.UserInfo
import kotlinx.coroutines.launch

@Composable
fun VerticalGrid(
	header: String,
	items: List<Item>,
	modifier: Modifier = Modifier,
	favoriteItems: List<Item>,
	showOriginalPrice: Boolean = false,
	snackbarHostState: SnackbarHostState,
	onFavorite: (Item) -> Unit,
	addToCart: (Item) -> Unit,
	onItemClick: (String) -> Unit
) {
	val scope = rememberCoroutineScope()
	val context = LocalContext.current

	Column {
		Text(
			text = header,
			modifier = modifier.padding(start = 10.dp),
			style = MaterialTheme.typography.headlineMedium
		)
		LazyVerticalGrid(
			columns = GridCells.Adaptive(150.dp),
			modifier = modifier.padding(start = 10.dp)
		) {
			items(items.size) { index ->
				Product(
					item = items[index],
					favoriteItems = favoriteItems,
					favorite = onFavorite,
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
fun CartItems(
	cartItems: List<CartItem>,
	modifier: Modifier = Modifier,
	paddingValues: PaddingValues,
	inCheckoutScreen: Boolean = false,
	userInfo: UserInfo = UserInfo(),
	add: (CartItem) -> Unit = {},
	remove: (CartItem) -> Unit = {},
	removeFromCart: (CartItem) -> Unit = {}
) {
	val layoutDirection = LocalLayoutDirection.current

	LazyColumn(
		modifier = modifier
			.fillMaxSize()
			.padding(16.dp),
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
					onButtonClick = {}
				)
			}
		}
		items(cartItems.size) { index ->
			CartItem(
				item = cartItems[index],
				inCheckoutScreen = inCheckoutScreen,
				add = add,
				remove = remove,
				removeFromCart = removeFromCart
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
	add: (CartItem) -> Unit,
	remove: (CartItem) -> Unit,
	removeFromCart: (CartItem) -> Unit
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.border(
				width = 2.dp,
				color = colorScheme.onBackground.copy(alpha = 0.5f),
				shape = shapes.extraLarge
			)
			.clip(shapes.extraLarge),
		verticalAlignment = Alignment.CenterVertically
	) {
		SmallPoster(url = item.photo.url, contentDescription = item.name)
		Column {
			Text(text = item.name)
			Row(verticalAlignment = Alignment.CenterVertically) {
				val colorValue = item.color.getColor()

				ColorBox(colorValue)
				Text(
					text = stringResource(colorValue.nameRes),
					modifier = Modifier.alpha(0.5f),
					fontSize = 8.sp
				)
				VerticalDivider(Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
				Text(stringResource(R.string.size))
				Text(
					text = item.size.toString(),
					modifier = Modifier
						.padding(start = 2.dp)
						.background(colorScheme.onBackground.copy(alpha = 0.1f))
						.padding(2.dp)
						.clip(shape = shapes.extraSmall)
				)
			}
			Row(verticalAlignment = Alignment.CenterVertically) {
				QuantityButton(
					itemSize = item.numberInCart.toString(),
					inCheckoutScreen = inCheckoutScreen,
					add = { add(item) },
					remove = { remove(item) }
				)
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
		Text(text = header, fontSize = 15.sp)
		Spacer(Modifier.weight(1f))
		if (showButton) {
			Text(
				text = stringResource(R.string.edit),
				modifier = Modifier.clickable { onButtonClick() },
				color = SkyBlue,
				fontSize = 15.sp
			)
		}
	}
}

@Composable
fun Container(
	header: String,
	modifier: Modifier = Modifier,
	showNavigateButton: Boolean = true,
	navigate: () -> Unit = {},
	content: @Composable ColumnScope.() -> Unit
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.clip(shapes.large)
			.background(SkyBlue.copy(alpha = 0.3f))
			.padding(16.dp)
	) {
		SmallHeader(
			header = header,
			showButton = showNavigateButton,
			onButtonClick = navigate
		)
		content()
	}
}

@Composable
private fun OrderDetails(
	cartItems: List<CartItem>,
	modifier: Modifier = Modifier
) {
	val subTotal = cartItems.sumOf { it.currentPrice.toInt() }
	val deliveryFee = ((5 / 100.0) * subTotal).toFloat()
	val discount = ((3 / 100.0) * subTotal).toFloat()
	val total = subTotal + deliveryFee - discount

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
				Row {
					Text("€", Modifier.padding(end = 10.dp))
					Text("$subTotal")
				}
				Row {
					Text("€", Modifier.weight(1f))
					Text("$deliveryFee")
				}
				Row {
					Text("€", Modifier.weight(1f))
					Text("$discount")
				}
			}
		}
		HorizontalDivider(Modifier.padding(vertical = 20.dp))
		Row(Modifier.fillMaxWidth()) {
			Text(stringResource(R.string.total), Modifier.alpha(0.7f))
			Row {
				Text("€", Modifier.padding(end = 10.dp))
				Text("$total")
			}
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
			.background(colorScheme.onBackground.copy(alpha = 0.1f))
			.padding(horizontal = 6.dp, vertical = 2.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = if (inCheckoutScreen) stringResource(R.string.quantity) else "-",
			modifier = Modifier.clickable(!inCheckoutScreen) { remove() }
		)
		Text(
			text = itemSize,
			modifier = Modifier
				.padding(4.dp)
				.background(colorScheme.onBackground.copy(alpha = 0.1f))
				.padding(4.dp)
				.clip(shape = shapes.extraSmall)
		)
		if (!inCheckoutScreen) {
			Text("+", Modifier.clickable { add() })
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
				modifier = Modifier.padding(end = 8.dp),
				tint = colorScheme.primary
			)
		}
		Text(text = label, fontSize = 12.sp)
	}
}
