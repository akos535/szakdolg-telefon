package com.remote.phone.panels

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remote.phone.ConnectionViewModel

@Composable
fun TouchpadControlPanel(connectionViewModel: ConnectionViewModel = viewModel()) {
	Column(

	) {
		Box(
			modifier = Modifier
				.padding(20.dp)
				.fillMaxWidth()
				.aspectRatio(1f)
				.dropShadow(
					shape = RoundedCornerShape(12.dp),
					shadow = Shadow(
						radius = 8.dp,
						color = Color.Black,
						offset = DpOffset((4).dp, (4).dp)
					)
				)
				.background(
					color = Color.DarkGray,
					shape = RoundedCornerShape(12.dp)
				)
				.pointerInput(connectionViewModel) {
					detectDragGestures { change, dragAmount ->
						change.consume()

						connectionViewModel.moveMouse(dragAmount.x, dragAmount.y)
					}
				}
		){}

		Row(
			horizontalArrangement = Arrangement.SpaceEvenly,
			modifier = Modifier.padding(horizontal = 15.dp)
		) {
			Button(
				onClick = {connectionViewModel.mouseClick("left")},
				modifier = Modifier.weight(1f)
					.padding(5.dp)
					.dropShadow(
						shape = RoundedCornerShape(12.dp),
						shadow = Shadow(
							radius = 8.dp,
							color = Color.Black,
							offset = DpOffset((4).dp, (4).dp)
						)
					),
				colors = ButtonDefaults.buttonColors(Color.DarkGray),
				shape = RoundedCornerShape(8.dp)
			) { }
			Button(
				onClick = {connectionViewModel.mouseClick("right")},
				modifier = Modifier.weight(1f)
					.padding(5.dp)
					.dropShadow(
						shape = RoundedCornerShape(8.dp),
						shadow = Shadow(
							radius = 8.dp,
							color = Color.Black,
							offset = DpOffset((4).dp, (4).dp)
						)
					),
				colors = ButtonDefaults.buttonColors(Color.DarkGray),
				shape = RoundedCornerShape(8.dp)
			) { }
		}
	}

}