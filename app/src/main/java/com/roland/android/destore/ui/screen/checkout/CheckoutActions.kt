package com.roland.android.destore.ui.screen.checkout

import com.roland.android.domain.data.UserInfo

sealed class CheckoutActions {

	data class UpdateUserInfo(val userInfo: UserInfo) : CheckoutActions()

	data object Checkout : CheckoutActions()

}