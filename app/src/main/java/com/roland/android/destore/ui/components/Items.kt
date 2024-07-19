package com.roland.android.destore.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.AsyncImagePainter.State.Empty
import coil.request.ImageRequest
import com.roland.android.destore.R
import com.roland.android.destore.ui.navigation.AppRoute
import com.roland.android.destore.utils.Extensions.getColor
import com.roland.android.destore.utils.Extensions.round
import com.roland.android.destore.utils.painterPlaceholder
import com.roland.android.domain.data.CartItem
import com.roland.android.domain.data.Item
import com.roland.android.remotedatasource.utils.Constant.BASE_IMAGE_URL

typealias ItemId = String
typealias ItemPrice = String

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Product(
	item: Item,
	modifier: Modifier = Modifier,
	favoriteItems: List<Item>,
	showOriginalPrice: Boolean,
	favorite: (Item) -> Unit,
	addToCart: (Item) -> Unit,
	onItemClick: (ItemId, ItemPrice) -> Unit
) {
	Column(
		modifier = modifier
			.padding(end = 10.dp, bottom = 10.dp)
			.clip(shapes.large)
			.clickable { onItemClick(item.id, item.currentPrice) }
			.padding(8.dp)
	) {
		Box(contentAlignment = Alignment.TopEnd) {
			MediumPoster(
				url = item.photos.last().url, // as it's impossible to delete images from the API, this fetches the last added image url
				contentDescription = item.name
			)
			FavoriteIconButton(item.isFavorite(favoriteItems)) { favorite(item) }
		}
		Text(
			text = item.category.name,
			modifier = Modifier.padding(top = 4.dp),
			softWrap = false,
			style = MaterialTheme.typography.bodySmall
		)
		Text(
			text = item.name,
			modifier = Modifier.basicMarquee(),
			fontSize = 18.sp,
			fontWeight = FontWeight.Medium,
			softWrap = false
		)
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			Column {
				Text(
					text = "€" + item.currentPrice,
					modifier = Modifier.padding(top = 6.dp),
					color = Color.Blue
				)
				if (showOriginalPrice) {
					val currentPrice = item.currentPrice.replace(",", "").toDouble()
					val originalPrice = ((12 / currentPrice) * 100 + currentPrice).round()
					Text(
						text = "€$originalPrice",
						modifier = Modifier.alpha(0.7f),
						textDecoration = TextDecoration.LineThrough
					)
				}
			}
			Spacer(Modifier.weight(1f))
			AddToCartIconButton { addToCart(item) }
		}
	}
}

@Composable
fun CartItem(
	item: CartItem,
	modifier: Modifier = Modifier,
	screen: AppRoute,
	numberInCart: Int,
	add: (CartItem) -> Unit,
	remove: (CartItem) -> Unit,
	removeFromCart: (CartItem) -> Unit,
	viewDetails: (ItemId, ItemPrice) -> Unit
) {
	val inCheckoutScreen = screen is AppRoute.CheckoutScreen
	val inCartScreen = screen is AppRoute.CartScreen

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
			Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
				Text(text = item.name, modifier = Modifier.weight(1f))
				if (inCartScreen) {
					IconButton(onClick = { removeFromCart(item) }) {
						Icon(
							imageVector = Icons.Rounded.Clear,
							contentDescription = stringResource(R.string.remove_from_cart),
							modifier = Modifier.scale(0.8f)
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
						.size(2.dp, 12.dp)
						.clip(shapes.small)
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
					canModifyCart = inCartScreen,
					add = { add(item) },
					remove = { remove(item) }
				)
				if (inCheckoutScreen) {
					Box(
						modifier = Modifier
							.padding(horizontal = 6.dp, vertical = 2.dp)
							.size(2.dp, 12.dp)
							.clip(shapes.small)
							.background(colorScheme.onBackground.copy(alpha = 0.5f))
					)
				}
				Text("€" + item.currentPrice, Modifier.padding(start = 4.dp))
			}
		}
	}
}

@Composable
fun SmallPoster(
	url: String,
	contentDescription: String,
	modifier: Modifier = Modifier
) {
	Poster(
		url = url,
		contentDescription = contentDescription,
		modifier = modifier.size(100.dp, 120.dp)
	)
}

@Composable
fun MediumPoster(
	url: String,
	contentDescription: String,
	modifier: Modifier = Modifier
) {
	Poster(
		url = url,
		contentDescription = contentDescription,
		modifier = modifier.height(150.dp)
	)
}

@Composable
fun LargePoster(
	url: String,
	contentDescription: String?,
	modifier: Modifier = Modifier
) {
	Poster(
		url = url,
		contentDescription = contentDescription,
		modifier = modifier
	)
}

@Composable
private fun Poster(
	url: String,
	contentDescription: String?,
	modifier: Modifier = Modifier
) {
	val state = remember { mutableStateOf<AsyncImagePainter.State>(Empty) }

	Box(modifier.clip(shapes.large)) {
		AsyncImage(
			model = ImageRequest.Builder(LocalContext.current)
				.data(BASE_IMAGE_URL + url)
				.crossfade(true)
				.build(),
			contentDescription = contentDescription,
			modifier = Modifier
				.fillMaxSize()
				.painterPlaceholder(state.value),
			onState = { state.value = it },
			contentScale = ContentScale.Crop
		)
	}
}