package com.roland.android.destore.utils

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.roland.android.destore.utils.AnimationDirection.LeftRight
import com.roland.android.destore.utils.AnimationDirection.SlideOutLeft
import com.roland.android.destore.utils.AnimationDirection.UpDown

// navigation animation
fun NavGraphBuilder.animatedComposable(
	route: String,
	animationDirection: AnimationDirection = LeftRight,
	arguments: List<NamedNavArgument> = emptyList(),
	deepLinks: List<NavDeepLink> = emptyList(),
	content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) = composable(
	route = route,
	arguments = arguments,
	deepLinks = deepLinks,
	enterTransition = {
		when (animationDirection) {
			LeftRight -> {
				slideInHorizontally(tween(500)) { it }
			}
			UpDown -> {
				slideInVertically(tween(500)) { it }
			}
			else -> null
		}
	},
	exitTransition = {
		if (animationDirection == SlideOutLeft) {
			slideOutHorizontally(tween(500)) { -it }
		} else null
	},
	popEnterTransition = null,
	popExitTransition = {
		when (animationDirection) {
			LeftRight -> {
				slideOutHorizontally(tween(500)) { it }
			}
			UpDown -> {
				slideOutVertically(tween(500)) { it }
			}
			else -> null
		}
	},
	content = content
)

enum class AnimationDirection {
	LeftRight, UpDown, SlideOutLeft
}