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
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.destore.R
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
	subTitle: String? = null,
	title: String = "",
	buttonText: String,
	navigate: (Screens) -> Unit = {}
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(20.dp, 16.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Box {
			subTitle?.let {
				Column {
					Text(text = it, modifier = Modifier.alpha(0.5f), fontSize = 12.sp)
					Text(text = title, fontSize = 19.sp)
				}
			} ?: TextButton(
				onClick = { navigate(Screens.Back) },
				shape = RoundedCornerShape(8.dp)
			) {
				Text(text = stringResource(R.string.cancel))
			}
		}
		Spacer(Modifier.weight(1f))
		Button(
			onClick = { navigate(Screens.CheckoutScreen) },
			shape = RoundedCornerShape(8.dp)
		) {
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
		colors = IconButtonDefaults.iconButtonColors(
			containerColor = if (itemIsFavorite) Color.Red else Color.Transparent,
			contentColor = if (itemIsFavorite) Color.White else Color.Transparent
		)
	) {
		Icon(
			imageVector = Icons.Rounded.FavoriteBorder,
			contentDescription = stringResource(if (itemIsFavorite) R.string.favorite else R.string.unfavorite),
			tint = Color.Transparent,
		)
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
			imageVector = Icons.Rounded.ShoppingBag,
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
			.size(24.dp)
			.background(color.color)
			.padding(2.dp)
			.clip(MaterialTheme.shapes.small)
	) {
		if (selected) {
			val colorName = stringResource(color.nameRes)
			Icon(
				imageVector = Icons.Rounded.Done,
				contentDescription = stringResource(R.string.color_is_selected, colorName),
				modifier = Modifier.clickable { onSelect(color) })
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