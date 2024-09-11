@file:OptIn(ExperimentalMaterial3Api::class)

package com.roland.android.destore.ui.screen.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.ContactPhone
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.roland.android.destore.R
import com.roland.android.destore.ui.components.InputTextField
import com.roland.android.domain.data.UserInfo

@Composable
fun InputUserInfoSheet(
	userInfo: UserInfo,
	onInfoChange: (UserInfo) -> Unit,
	onDismiss: () -> Unit
) {
	val sheetState = rememberModalBottomSheetState(true)
	var name by rememberSaveable { mutableStateOf(userInfo.name) }
	var email by rememberSaveable { mutableStateOf(userInfo.email) }
	var phone by rememberSaveable { mutableStateOf(userInfo.phone) }

	ModalBottomSheet(
		onDismissRequest = onDismiss,
		modifier = Modifier.imePadding(),
		sheetState = sheetState
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
		) {
			InputTextField(
				label = stringResource(R.string.name_label),
				text = name,
				placeholder = stringResource(R.string.name_placeholder),
				leadingIcon = Icons.Rounded.Person,
				keyboardType = KeyboardType.Text,
				onValueChange = { name = it }
			)
			InputTextField(
				label = stringResource(R.string.email_label),
				text = email,
				placeholder = stringResource(R.string.email_placeholder),
				leadingIcon = Icons.Rounded.AlternateEmail,
				keyboardCapitalization = KeyboardCapitalization.None,
				keyboardType = KeyboardType.Email,
				onValueChange = { email = it }
			)
			InputTextField(
				label = stringResource(R.string.phone_label),
				text = phone,
				placeholder = stringResource(R.string.phone_placeholder),
				leadingIcon = Icons.Rounded.ContactPhone,
				keyboardType = KeyboardType.Phone,
				onValueChange = { phone = it }
			)
			ActionButtons(
				canSave = name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty(),
				onDismiss = onDismiss,
				onSave = {
					val newUserInfo = UserInfo(name, email, phone, userInfo.address)
					onInfoChange(newUserInfo)
					onDismiss()
				}
			)
		}
	}
}

@Composable
fun InputAddressSheet(
	address: String,
	onAddressChange: (String) -> Unit,
	onDismiss: () -> Unit
) {
	val sheetState = rememberModalBottomSheetState(true)
	var newAddress by rememberSaveable { mutableStateOf(address) }

	ModalBottomSheet(
		onDismissRequest = onDismiss,
		sheetState = sheetState
	) {
		Column(Modifier.fillMaxWidth()) {
			InputTextField(
				label = stringResource(R.string.address_label),
				text = newAddress,
				placeholder = stringResource(R.string.address_placeholder),
				leadingIcon = Icons.Rounded.Home,
				keyboardType = KeyboardType.Text,
				onValueChange = { newAddress = it }
			)
			ActionButtons(
				canSave = newAddress.isNotEmpty() && newAddress != address,
				onDismiss = onDismiss,
				onSave = {
					onAddressChange(newAddress)
					onDismiss()
				}
			)
		}
	}
}

@Composable
private fun ActionButtons(
	canSave: Boolean,
	onDismiss: () -> Unit,
	onSave: () -> Unit
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp)
			.padding(top = 20.dp)
	) {
		TextButton(
			onClick = onDismiss,
			modifier = Modifier.weight(1f)
		) {
			Text(stringResource(R.string.cancel))
		}
		Spacer(Modifier.width(10.dp))
		Button(
			onClick = onSave,
			modifier = Modifier.weight(1f),
			enabled = canSave
		) {
			Text(stringResource(R.string.save))
		}
	}
}
