package com.remote.phone

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ConnectPage(connectionViewModel: ConnectionViewModel = viewModel()) {

	val computers by connectionViewModel.discoveredPcs.collectAsState()

	Column(
		modifier = Modifier.padding(40.dp)
	) {
		val connViewModel: ConnectionViewModel = viewModel()
		Button(
			onClick = { connViewModel.connect("192.168.1.57") },
			enabled = connViewModel.status.collectAsState().value == "Disconnected",
			modifier = Modifier.padding(top = 30.dp).fillMaxWidth()
		) {
			Text("Connect.")
		}
		Button(
			onClick = { connViewModel.disconnect() },
			enabled = connViewModel.status.collectAsState().value == "Connected",
			modifier = Modifier.padding(top = 60.dp).fillMaxWidth()
		) {
			Text("Disconnect.")
		}

		Button(
			onClick = { connectionViewModel.discoverPcs() },
			modifier = Modifier.padding(top = 60.dp).fillMaxWidth()
		) {
			Text("PC search")
		}

		computers.forEach { pc ->
			Button(onClick = { connectionViewModel.connect(pc.ip) }) {
				Text("${pc.name} — ${pc.ip}")
			}
		}
	}

}