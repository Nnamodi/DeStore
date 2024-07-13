package com.roland.android.destore.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.AsyncImagePainter.State.Empty
import coil.request.ImageRequest
import com.roland.android.destore.utils.painterPlaceholder
import com.roland.android.remotedatasource.usecase.data.Item
import com.roland.android.remotedatasource.utils.Constant.BASE_IMAGE_URL

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Product(
	item: Item,
	modifier: Modifier = Modifier,
	favoriteItems: List<Item>,
	showOriginalPrice: Boolean,
	favorite: (Item) -> Unit,
	addToCart: (Item) -> Unit,
	onItemClick: (String) -> Unit
) {
	Column(
		modifier = modifier
			.padding(end = 10.dp, bottom = 10.dp)
			.clip(MaterialTheme.shapes.large)
			.clickable { onItemClick(item.id) }
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
			modifier = Modifier
				.padding(horizontal = 10.dp)
				.basicMarquee(),
			fontSize = 8.sp,
			softWrap = false
		)
		Text(
			text = item.name,
			modifier = Modifier
				.padding(horizontal = 10.dp)
				.basicMarquee(),
			fontSize = 18.sp,
			fontWeight = FontWeight.Medium,
			softWrap = false
		)
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.Bottom
		) {
			Column {
				Text(
					text = "€" + item.currentPrice,
					modifier = Modifier.padding(start = 10.dp, bottom = 20.dp),
					color = Color.Blue
				)
				if (showOriginalPrice) {
					val originalPrice = ((12 / item.currentPrice.toInt()) * 100 + item.currentPrice.toInt())
					Text(
						text = "€$originalPrice",
						modifier = Modifier
							.padding(start = 10.dp, bottom = 20.dp)
							.alpha(0.7f),
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

	Box(modifier.clip(MaterialTheme.shapes.large)) {
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