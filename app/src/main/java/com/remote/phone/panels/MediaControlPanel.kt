package com.remote.phone.panels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeDown
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remote.phone.ConnectionViewModel

@Composable
fun MediaControlPanel(connectionViewModel: ConnectionViewModel = viewModel()) {
	Column(
		modifier = Modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.Bottom
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
		) {

		}

		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(20.dp),
			horizontalArrangement = Arrangement.Center
		) {
			Box(
				modifier = Modifier.weight(1f),
				contentAlignment = Alignment.Center
			) {
				FilledIconButton(
					onClick = { connectionViewModel.keypress("VOLUME_MUTE") },
					modifier = Modifier
						.size(60.dp)
						.dropShadow(
						shape = CircleShape,
						shadow = Shadow(
							radius = 8.dp,
							color = Color.Black,
							offset = DpOffset(4.dp, 4.dp)
						)
					),
					shape = CircleShape,
					colors = IconButtonDefaults.filledIconButtonColors(
						containerColor = Color.Gray,
						contentColor = Color.White
					)
				) {
					Icon(
						imageVector = Icons.AutoMirrored.Filled.VolumeOff,
						contentDescription = "Mute Sound"
					)
				}
			}

			Box(
				modifier = Modifier.weight(1f),
				contentAlignment = Alignment.Center
			) {
				FilledIconButton(
					onClick = { connectionViewModel.keypress("VOLUME_DOWN") },
					modifier = Modifier
						.size(60.dp)
						.dropShadow(
							shape = CircleShape,
							shadow = Shadow(
								radius = 8.dp,
								color = Color.Black,
								offset = DpOffset(4.dp, 4.dp)
							)
						),
					shape = CircleShape,
					colors = IconButtonDefaults.filledIconButtonColors(
						containerColor = Color.Gray,
						contentColor = Color.White
					)
				) {
					Icon(
						imageVector = Icons.AutoMirrored.Filled.VolumeDown,
						contentDescription = "Volume Down"
					)
				}
			}

			Box(
				modifier = Modifier.weight(1f),
				contentAlignment = Alignment.Center
			) {
				FilledIconButton(
					onClick = { connectionViewModel.keypress("VOLUME_UP") },
					modifier = Modifier
						.size(60.dp)
						.dropShadow(
							shape = CircleShape,
							shadow = Shadow(
								radius = 8.dp,
								color = Color.Black,
								offset = DpOffset(4.dp, 4.dp)
							)
						),
					shape = CircleShape,
					colors = IconButtonDefaults.filledIconButtonColors(
						containerColor = Color.Gray,
						contentColor = Color.White
					)
				) {
					Icon(
						imageVector = Icons.AutoMirrored.Filled.VolumeUp,
						contentDescription = "Volume Up"
					)
				}
			}
		}
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(20.dp),
			horizontalArrangement = Arrangement.SpaceEvenly
		) {
			Box(
				modifier = Modifier.weight(1f),
				contentAlignment = Alignment.Center
			) {
				FilledIconButton(
					onClick = { connectionViewModel.keypress("MEDIA_PREV_TRACK") },
					modifier = Modifier
						.size(60.dp)
						.dropShadow(
							shape = CircleShape,
							shadow = Shadow(
								radius = 8.dp,
								color = Color.Black,
								offset = DpOffset(4.dp, 4.dp)
							)
						),
					shape = CircleShape,
					colors = IconButtonDefaults.filledIconButtonColors(
						containerColor = Color.Gray,
						contentColor = Color.White
					)
				) {
					Icon(
						imageVector = Icons.Filled.SkipPrevious,
						contentDescription = "Previous track"
					)
				}
			}

			Box(
				modifier = Modifier.weight(1f),
				contentAlignment = Alignment.Center
			) {
				FilledIconButton(
					onClick = { connectionViewModel.keypress("MEDIA_PLAY_PAUSE") },
					modifier = Modifier
						.size(60.dp)
						.dropShadow(
							shape = CircleShape,
							shadow = Shadow(
								radius = 8.dp,
								color = Color.Black,
								offset = DpOffset(4.dp, 4.dp)
							)
						),
					shape = CircleShape,
					colors = IconButtonDefaults.filledIconButtonColors(
						containerColor = Color.Gray,
						contentColor = Color.White
					)
				) {
					Icon(
						imageVector = Icons.Filled.PlayArrow,
						contentDescription = "Play/Pause"
					)
				}
			}

			Box(
				modifier = Modifier.weight(1f),
				contentAlignment = Alignment.Center
			) {
				FilledIconButton(
					onClick = { connectionViewModel.keypress("MEDIA_NEXT_TRACK") },
					modifier = Modifier
						.size(60.dp)
						.dropShadow(
							shape = CircleShape,
							shadow = Shadow(
								radius = 8.dp,
								color = Color.Black,
								offset = DpOffset(4.dp, 4.dp)
							)
						),
					shape = CircleShape,
					colors = IconButtonDefaults.filledIconButtonColors(
						containerColor = Color.Gray,
						contentColor = Color.White
					)
				) {
					Icon(
						imageVector = Icons.Filled.SkipNext,
						contentDescription = "Next track"
					)
				}
			}
		}
	}
}
