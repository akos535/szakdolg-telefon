package com.remote.phone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ConnectPage(connectionViewModel: ConnectionViewModel = viewModel()) {

	val computers by connectionViewModel.discoveredPcs.collectAsState()
	val connViewModel: ConnectionViewModel = viewModel()
	val connected by connectionViewModel.connected.collectAsState()
	var searched = false

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.background(Color(100, 100,255), shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
	) {
		Text(
			if(connected == "") {
				"Not connected"
			} else {
				"Connected to: $connected"
			},
			modifier = Modifier.padding(top = 40.dp, bottom = 10.dp).fillMaxWidth(),
			textAlign = TextAlign.Center
		)
	}

	Column(
		modifier = Modifier.padding(40.dp)
	) {

		Button(
			onClick = {
				connectionViewModel.discoverPcs()
				searched = true
					  },
			modifier = Modifier.padding(top = 60.dp).fillMaxWidth()
		) {
			Text("PC search")
		}

		computers.forEach { pc ->
			Button(
				onClick = {
					connectionViewModel.connect(pc.ip, pc.name, pc.webSocketPort)
						  }
			) {
				Text("${pc.name} — ${pc.ip}")
			}
		}

		Box (
			modifier = Modifier.fillMaxWidth()
		) {
			Text(
				if(searched && computers.isEmpty()) {
					"No computers have been found."
				} else {
					""
				},
				color = Color.Red
			)
		}


		Button(
			onClick = { connViewModel.disconnect() },
			enabled = connViewModel.status.collectAsState().value == "Connected",
			modifier = Modifier.padding(top = 60.dp).fillMaxWidth()
		) {
			Text("Disconnect.")
		}
	}

}