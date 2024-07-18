package com.roland.android.domain.usecase

import com.roland.android.domain.repository.local.ItemId
import com.roland.android.domain.repository.local.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WishlistUseCase : UseCase<WishlistUseCase.Request, WishlistUseCase.Response>(), KoinComponent {

	private val wishlistRepository: WishlistRepository by inject()

	override fun process(request: Request): Flow<Response> {
		val actionFlow = when (request.action) {
			WishlistUseCaseActions.GetWishlistItems -> wishlistRepository.getAllItems()
			is WishlistUseCaseActions.AddToWishlist -> wishlistRepository.addToWishlist(request.action.wishlistItem)
			is WishlistUseCaseActions.RemoveFromWishlist -> wishlistRepository.removeFromWishlist(request.action.wishlistItem)
		}
		return actionFlow.map { Response(it) }
	}

	data class Request(val action: WishlistUseCaseActions) : UseCase.Request

	data class Response(val wishlistItems: List<ItemId>) : UseCase.Response

}

sealed class WishlistUseCaseActions {

	data object GetWishlistItems : WishlistUseCaseActions()

	data class AddToWishlist(val wishlistItem: ItemId) : WishlistUseCaseActions()

	data class RemoveFromWishlist(val wishlistItem: ItemId) : WishlistUseCaseActions()

}