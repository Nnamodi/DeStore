package com.roland.android.destore.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CardTravel
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.destore.R
import com.roland.android.destore.ui.navigation.AppRoute
import com.roland.android.destore.ui.navigation.Screens
import com.roland.android.destore.ui.theme.Black
import com.roland.android.destore.ui.theme.Brown
import com.roland.android.destore.ui.theme.Green
import com.roland.android.destore.ui.theme.Purple
import com.roland.android.destore.ui.theme.Red
import com.roland.android.destore.ui.theme.SkyBlue
import com.roland.android.destore.ui.theme.Yellow

@Composable
fun FixedBottomButton(
	modifier: Modifier = Modifier,
	subTitle: String = "",
	title: String = "",
	screen: AppRoute,
	buttonText: String,
	onButtonClick: () -> Unit = {},
	navigate: (Screens) -> Unit = {}
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.background(MaterialTheme.colorScheme.surface)
			.padding(20.dp, 16.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Box {
			if (screen !is AppRoute.CheckoutScreen) {
				Column {
					Text(text = subTitle, modifier = Modifier.alpha(0.5f), fontSize = 12.sp)
					Text(text = title, fontSize = 19.sp)
				}
			} else TextButton(
				onClick = { navigate(Screens.Back) },
				shape = RoundedCornerShape(8.dp)
			) {
				Text(text = stringResource(R.string.cancel))
			}
		}
		Spacer(Modifier.weight(1f))
		Button(
			onClick = {
				when (screen) {
					is AppRoute.CartScreen -> navigate(Screens.CheckoutScreen)
					is AppRoute.CheckoutScreen -> {
						onButtonClick()
						navigate(Screens.OrderCompleteScreen)
					}
					is AppRoute.DetailsScreen -> onButtonClick()
					else -> {}
				}
			},
			shape = RoundedCornerShape(8.dp)
		) {
			if (screen is AppRoute.DetailsScreen) {
				Icon(Icons.Rounded.CardTravel, null, Modifier.padding(end = 8.dp))
			}
			Text(text = buttonText)
		}
	}
}

@Composable
fun FavoriteIconButton(
	itemIsFavorite: Boolean,
	favorite: () -> Unit
) {
	IconButton(
		onClick = favorite,
		modifier = Modifier.scale(0.8f),
		colors = IconButtonDefaults.iconButtonColors(
			containerColor = if (itemIsFavorite) Color.Red else Color.Black.copy(alpha = 0.5f),
			contentColor = if (itemIsFavorite) Color.White else Color.Black.copy(alpha = 0.5f)
		)
	) {
		Icon(
			imageVector = Icons.Rounded.FavoriteBorder,
			contentDescription = stringResource(if (itemIsFavorite) R.string.favorite else R.string.unfavorite),
			tint = Color.White,
		)
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
				text = "—",
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
				fontSize = 20.sp
			)
		}
	}
}

@Composable
fun AddToCartIconButton(
	addToCart: () -> Unit
) {
	Surface(
		onClick = addToCart,
		shape = RoundedCornerShape(6.dp),
		color = SkyBlue.copy(alpha = 0.4f)
	) {
		Icon(
			imageVector = Icons.Rounded.CardTravel,
			modifier = Modifier
				.scale(0.75f)
				.padding(5.dp, 3.dp),
			contentDescription = stringResource(R.string.add_to_cart),
			tint = SkyBlue
		)
	}
}

@Composable
fun ColorBox(
	color: Colors,
	modifier: Modifier = Modifier,
	selected: Boolean = false,
	onSelect: (Colors) -> Unit = {}
) {
	Box(
		modifier
			.animateContentSize()
			.size(28.dp)
			.padding(2.dp)
			.clip(MaterialTheme.shapes.small)
			.background(color.color)
			.clickable { onSelect(color) }
			.padding(2.dp)
	) {
		if (selected) {
			val colorName = stringResource(color.nameRes)
			Icon(
				imageVector = Icons.Rounded.Done,
				contentDescription = stringResource(R.string.color_is_selected, colorName),
				modifier = Modifier.scale(0.85f),
				tint = Color.White
			)
		}
	}
}

enum class Colors(
	@StringRes val nameRes: Int,
	val color: Color
) {
	ColorBrown(R.string.brown, Brown),
	ColorPurple(R.string.purple, Purple),
	ColorGreen(R.string.green, Green),
	ColorBlue(R.string.blue, SkyBlue),
	ColorYellow(R.string.yellow, Yellow),
	ColorRed(R.string.red, Red),
	ColorBlack(R.string.black, Black)
}