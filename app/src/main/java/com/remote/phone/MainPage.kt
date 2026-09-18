package com.remote.phone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.remote.phone.panels.MediaControlPanel
import com.remote.phone.panels.TouchpadControlPanel

@Composable
fun MainPage(
	selectedOption: ControlPanels,
	onMenuOptionSelected: (ControlPanels) -> Unit
) {
	var expanded by remember { mutableStateOf(false) }
	var current = selectedOption
	Column(

	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.background(Color(100, 100,255), shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
		) {
			Row(
				modifier = Modifier
					.clickable(
						onClick = {expanded = !expanded}
					)
					.padding(start = 10.dp, top = 40.dp, end = 10.dp, bottom = 10.dp)
					.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically
			) {
				Text( text = current.label )
				Icon(
					imageVector = Icons.Default.KeyboardArrowDown,
					contentDescription = "More Options",
					modifier = Modifier.padding(end = 10.dp)
				)
			}
			DropdownMenu(
				expanded = expanded,
				onDismissRequest = { expanded = false }
			) {
				ControlPanels.entries.forEach { it ->
					DropdownMenuItem(
						text = {Text(it.label)},
						onClick = {
							onMenuOptionSelected(it)
							expanded = !expanded
						}
					)
				}
			}
		}
		Box(
			modifier = Modifier.fillMaxSize()
		) {
			current.content()
		}
	}
}

enum class ControlPanels(
	var label: String,
	val content: @Composable () -> Unit
) {
	MEDIA("Media", { MediaControlPanel() }),
	TOUCHPAD("Touchpad", { TouchpadControlPanel() })
}