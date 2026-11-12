package com.remote.phone.panels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remote.phone.ConnectionViewModel

private enum class KeyboardPage {
	Letters,
	Numbers,
	AccentedLetters
}


@Composable
fun KeyboardControlPanel(connectionViewModel: ConnectionViewModel = viewModel()) {

	var page by remember { mutableStateOf(KeyboardPage.Letters) }

	val topLetterKeys = listOf(
		"q" to "Q", "w" to "W", "e" to "E", "r" to "R", "t" to "T",
		"z" to "Z", "u" to "U", "i" to "I", "o" to "O",	"p" to "P"
	)

	val middleLetterKeys = listOf(
		"a" to "A", "s" to "S", "d" to "D", "f" to "F", "g" to "G",
		"h" to "H", "j" to "J", "k" to "K", "l" to "L"
	)

	val bottomLetterKeys = listOf(
		"y" to "Y", "x" to "X", "c" to "C", "v" to "V",
		"b" to "B", "n" to "N", "m" to "M"
	)

	val numberKeys = listOf(
		"0" to "ZERO", "1" to "ONE", "2" to "TWO", "3" to "THREE", "4" to "FOUR",
		"5" to "FIVE", "6" to "SIX", "7" to "SEVEN", "8" to "EIGHT", "9" to "NINE"
	)

	val accentedLetterKeys = listOf(
		"á" to "OEM_7", "é" to "OEM_1", "í" to "OEM_102", "ó" to "OEM_PLUS", "ö" to "OEM_3",
		"ő" to "OEM_4", "ú" to "OEM_6", "ü" to "OEM_2", "ű" to "OEM_5"
	)

	Column(
		modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp, vertical = 10.dp),
		verticalArrangement = Arrangement.Bottom,
		horizontalAlignment = Alignment.CenterHorizontally
	)
	{
		when(page) {
			KeyboardPage.Letters -> {
				KeyboardKeyRow(topLetterKeys, connectionViewModel)
				KeyboardKeyRow(middleLetterKeys, connectionViewModel)
				KeyboardKeyRow(bottomLetterKeys, connectionViewModel)
			}

			KeyboardPage.Numbers -> {
				KeyboardKeyRow(numberKeys.take(5), connectionViewModel)
				KeyboardKeyRow(numberKeys.drop(5), connectionViewModel)
			}

			KeyboardPage.AccentedLetters -> {
				KeyboardKeyRow(accentedLetterKeys.take(5), connectionViewModel)
				KeyboardKeyRow(accentedLetterKeys.drop(5), connectionViewModel)
			}
		}

		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.spacedBy(4.dp)
		) {
			Button(
				onClick = { page = KeyboardPage.Letters},
				modifier = Modifier.weight(1f),
				contentPadding = PaddingValues(0.dp),
				shape = RoundedCornerShape(4.dp)
			) {
				Text("ABC")
			}

			Button(
				onClick = { page = KeyboardPage.Numbers},
				modifier = Modifier.weight(1f),
				contentPadding = PaddingValues(0.dp),
				shape = RoundedCornerShape(4.dp)
			) {
				Text("123")
			}

			Button(
				onClick = { page = KeyboardPage.AccentedLetters},
				modifier = Modifier.weight(1f),
				contentPadding = PaddingValues(0.dp),
				shape = RoundedCornerShape(4.dp)
			) {
				Text("ÁÉÍ")
			}
		}
	}
}

@Composable
private fun KeyboardKeyRow(
	keys: List<Pair<String,String>>,
	viewModel: ConnectionViewModel
) {
	Row(
		modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
		horizontalArrangement = Arrangement.spacedBy(2.dp)
	) {
		keys.forEach { (label, key) ->
			Button(
				onClick = {viewModel.keypress(key)},
				modifier = Modifier.weight(1f).height(48.dp),
				contentPadding = PaddingValues(0.dp),
				shape = RoundedCornerShape(4.dp),
				colors = ButtonDefaults.buttonColors(
					containerColor = Color.LightGray,
					contentColor = Color.Black
				)
			) {
				Text(text = label, maxLines = 1, fontSize = 16.sp)
			}
		}
	}
}
