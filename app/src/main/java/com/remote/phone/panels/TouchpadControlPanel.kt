package com.remote.phone.panels

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TouchpadControlPanel() {
	Text(
		text = "Touchpad controls",
		modifier = Modifier.padding(top = 20.dp)
	)
}