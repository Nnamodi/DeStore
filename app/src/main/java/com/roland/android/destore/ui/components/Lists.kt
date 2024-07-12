package com.roland.android.destore.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.roland.android.remotedatasource.usecase.data.Item

@Composable
fun VerticalGrid(
	header: String,
	items: List<Item>?,
	modifier: Modifier = Modifier,
	content: LazyGridScope.() -> Unit = {}
) {
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
			items?.let {
				items(it.size) { index ->
					Product(it[index])
				}
			} ?: content()
		}
	}
}