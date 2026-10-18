package com.remote.phone.panels

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remote.phone.ConnectionViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun MediaControlPanel(connectionViewModel: ConnectionViewModel = viewModel()) {
	Text(
		text = "Media controls",
		modifier = Modifier.padding(top = 20.dp)
	)
	Button(
		onClick = { connectionViewModel.playPause() },
		enabled = connectionViewModel.status.collectAsState().value == "Connected",
		modifier = Modifier.padding(top = 60.dp)
	) {
		Text("Play / Pause")
	}
}