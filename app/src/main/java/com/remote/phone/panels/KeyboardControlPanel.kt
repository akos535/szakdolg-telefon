package com.remote.phone.panels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remote.phone.ConnectionViewModel

@Composable
fun KeyboardControlPanel(connectionViewModel: ConnectionViewModel = viewModel()) {
	Column(
		modifier = Modifier.fillMaxSize().padding(start = 10.dp, end = 10.dp),
		verticalArrangement = Arrangement.Bottom,
		horizontalAlignment = Alignment.CenterHorizontally
	)
	{
		if(connectionViewModel.lang == "HU") {
			HUKeyboardRows(connectionViewModel)
		}
	}
}

@Composable
fun HUKeyboardRows(viewModel: ConnectionViewModel) {
	//replaced with language check later trust trust (will forget)
	val numberRowkeys = listOf(
		"0" to "ZERO",
		"1" to "ONE",
		"2" to "TWO",
		"3" to "THREE",
		"4" to "FOUR",
		"5" to "FIVE",
		"6" to "SIX",
		"7" to "SEVEN",
		"8" to "EIGHT",
		"9" to "NINE",
		"ö" to "OEM_3",
		"ü" to "OEM_2",
		"ó" to "OEM_PLUS"
	)
	val topLetterKeys = listOf(
		"q" to "Q",
		"w" to "W",
		"e" to "E",
		"r" to "R",
		"t" to "T",
		"z" to "Z",
		"u" to "U",
		"i" to "I",
		"o" to "O",
		"p" to "P",
		"ő" to "OEM_4",
		"ú" to "OEM_6"
	)
	val middleLetterKeys = listOf(
		"a" to "A",
		"s" to "S",
		"d" to "D",
		"f" to "F",
		"g" to "G",
		"h" to "H",
		"j" to "J",
		"k" to "K",
		"l" to "L",
		"é" to "OEM_1",
		"á" to "OEM_7",
		"ű" to "OEM_5"
	)
	val bottomLetterKeys = listOf(
		"í" to "OEM_102",
		"y" to "Y",
		"x" to "X",
		"c" to "C",
		"v" to "V",
		"b" to "B",
		"n" to "N",
		"m" to "M",
		"," to "OEM_COMMA",
		"." to "OEM_PERIOD",
		"-" to "OEM_MINUS"
	)

	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(2.dp)
	) {
		numberRowkeys.forEach { (label, key) ->
			Button(
				onClick = { viewModel.keypress(key) },
				modifier = Modifier.weight(1f),
				contentPadding = PaddingValues(0.dp),
				shape = RoundedCornerShape(4.dp),
				colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
			) {
				Text(
					text = label,
					maxLines = 1,
					fontSize = 12.sp
				)
			}
		}
	}
	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(2.dp)
	) {
		topLetterKeys.forEach { (label, key) ->
			Button(
				onClick = { viewModel.keypress(key) },
				modifier = Modifier.weight(1f),
				contentPadding = PaddingValues(0.dp),
				shape = RoundedCornerShape(4.dp),
				colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
			) {
				Text(label, maxLines = 1, fontSize = 12.sp)
			}
		}
	}

	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(2.dp)
	) {
		middleLetterKeys.forEach { (label, key) ->
			Button(
				onClick = { viewModel.keypress(key) },
				modifier = Modifier.weight(1f),
				contentPadding = PaddingValues(0.dp),
				shape = RoundedCornerShape(4.dp),
				colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
			) {
				Text(label, maxLines = 1, fontSize = 12.sp)
			}
		}
	}

	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(2.dp)
	) {
		bottomLetterKeys.forEach { (label, key) ->
			Button(
				onClick = { viewModel.keypress(key) },
				modifier = Modifier.weight(1f),
				contentPadding = PaddingValues(0.dp),
				shape = RoundedCornerShape(4.dp),
				colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
			) {
				Text(label, maxLines = 1, fontSize = 12.sp)
			}
		}
	}
}
