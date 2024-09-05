package com.roland.android.destore.ui.screen.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.ContactPhone
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.roland.android.destore.R
import com.roland.android.domain.data.UserInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
	onSignUp: (UserInfo) -> Unit,
	navigateHome: () -> Unit
) {
	val scope = rememberCoroutineScope()
	var name by rememberSaveable { mutableStateOf("") }
	var email by rememberSaveable { mutableStateOf("") }
	var phone by rememberSaveable { mutableStateOf("") }
	var address by rememberSaveable { mutableStateOf("") }

	Box(Modifier.fillMaxSize()) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState()),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Icon(
				painter = painterResource(R.drawable.app_icon),
				contentDescription = stringResource(R.string.app_name),
				modifier = Modifier.padding(top = 50.dp)
			)
			Text(
				text = stringResource(R.string.app_name),
				modifier = Modifier.padding(bottom = 30.dp),
				fontWeight = FontWeight.SemiBold,
				style = MaterialTheme.typography.headlineMedium
			)
			Text(
				text = stringResource(R.string.sign_up),
				modifier = Modifier.padding(bottom = 30.dp),
				fontStyle = FontStyle.Italic,
				fontWeight = FontWeight.SemiBold,
				style = MaterialTheme.typography.titleLarge
			)
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
			InputTextField(
				label = stringResource(R.string.address_label),
				text = address,
				placeholder = stringResource(R.string.address_placeholder),
				leadingIcon = Icons.Rounded.Home,
				keyboardType = KeyboardType.Text,
				onValueChange = { address = it }
			)
			Spacer(Modifier.height(50.dp))
		}
		ActionButtons(
			modifier = Modifier
				.fillMaxWidth()
				.align(Alignment.BottomCenter),
			signUpEnabled = name.trim().isNotEmpty(),
			onSkip = { navigateHome() },
			onSignUp = {
				val userInfo = UserInfo(
					name = name.trim(),
					email = email.trim(),
					phone = phone.trim(),
					address = address.trim()
				)
				scope.launch {
					onSignUp(userInfo)
					delay(3000)
					navigateHome()
				}
			}
		)
	}
}

@Composable
private fun InputTextField(
	label: String,
	text: String,
	placeholder: String,
	leadingIcon: ImageVector,
	keyboardType: KeyboardType,
	modifier: Modifier = Modifier,
	onValueChange: (String) -> Unit
) {
	Column(modifier) {
		OutlinedTextField(
			value = text,
			onValueChange = onValueChange,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 20.dp)
				.padding(top = 20.dp),
			label = { Text(label) },
			placeholder = { Text(placeholder, Modifier.alpha(0.7f)) },
			leadingIcon = { Icon(leadingIcon, label) },
			keyboardOptions = KeyboardOptions(
				capitalization = KeyboardCapitalization.Words,
				keyboardType = keyboardType
			),
			singleLine = true,
		)
	}
}

@Composable
private fun ActionButtons(
	modifier: Modifier = Modifier,
	signUpEnabled: Boolean,
	onSkip: () -> Unit,
	onSignUp: () -> Unit
) {
	val signUpClicked = rememberSaveable { mutableStateOf(false) }

	Row(
		modifier = modifier.padding(20.dp, 10.dp),
		horizontalArrangement = Arrangement.spacedBy(10.dp)
	) {
		TextButton(
			onClick = onSkip,
			modifier = Modifier.weight(1f),
			enabled = !signUpClicked.value
		) {
			Text(stringResource(R.string.skip))
		}
		Button(
			onClick = {
				signUpClicked.value = true
				onSignUp()
			},
			modifier = Modifier.weight(1f),
			enabled = signUpEnabled && !signUpClicked.value
		) {
			if (signUpClicked.value) {
				CircularProgressIndicator(
					modifier = Modifier.size(20.dp),
					strokeWidth = 2.dp
				)
			} else {
				Text(stringResource(R.string.sign_up))
			}
		}
	}
}
