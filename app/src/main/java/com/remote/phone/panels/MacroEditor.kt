package com.remote.phone.panels

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.remote.phone.data.storage.MacroStep
import com.remote.phone.data.storage.SavedMacro
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MacroEditor(
	initialMacro: SavedMacro?,
	onCancel: () -> Unit,
	onSave: (SavedMacro) -> Unit,
	onDelete: () -> Unit
) {
	val steps = remember(initialMacro?.id) { (initialMacro?.steps ?: emptyList()).toMutableStateList() }
	var macroName by remember(initialMacro?.id) {mutableStateOf(initialMacro?.name ?: "")}
	var showDeleteConfirmation by remember {mutableStateOf(false)}


	var rowHeightPx by remember { mutableIntStateOf(0) }

	val allStepsValid = steps.all { step ->
		when (step) {
			is MacroStep.KeyDown -> step.key.isNotBlank()
			is MacroStep.KeyUp -> step.key.isNotBlank()
			is MacroStep.Delay -> step.milliseconds.toIntOrNull()?.let { it in 0..60000 } == true
		}
	}

	val keys = listOf(
		"Shift" to "SHIFT",
		"Ctrl" to "CONTROL",
		"Enter" to "RETURN",
		"Win" to "LWIN",
		"Alt" to "LMENU",
		"AltGr" to "RMENU",
		"A" to "A", "B" to "B", "C" to "C", "D" to "D", "E" to "E", "F" to "F", "G" to "G", "H" to "H", "I" to "I",
		"J" to "J", "K" to "K", "L" to "L", "M" to "M", "N" to "N", "O" to "O", "P" to "P", "Q" to "Q", "R" to "R",
		"S" to "S", "T" to "T", "U" to "U", "V" to "V", "W" to "W", "X" to "X", "Y" to "Y", "Z" to "Z"
 	)

	Column(
		modifier = Modifier
			.fillMaxSize()
			.verticalScroll(rememberScrollState())
			.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		OutlinedTextField(
			value = macroName,
			onValueChange = {macroName = it},
			label = {Text("Macro name")},
			singleLine = true,
			modifier = Modifier.fillMaxWidth()
		)

		Text("Add a step:")

		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.spacedBy(4.dp)
		) {
			TextButton(
				onClick = { steps.add(MacroStep.KeyDown()) }
			) {
				Text("Key down")
			}

			TextButton(
				onClick = { steps.add(MacroStep.KeyUp()) }
			) {
				Text("Key up")
			}

			TextButton(
				onClick = { steps.add(MacroStep.Delay()) }
			) {
				Text("Delay")
			}
		}

		if (steps.isEmpty()) {
			Text("Your macro has no steps yet.")
		}

		steps.toList().forEach { step ->
			key(step.id) {
				Card(
					modifier = Modifier
						.fillMaxWidth()
						.height(88.dp)
						.onSizeChanged { size -> rowHeightPx = size.height }
				) {
					Row(
						modifier = Modifier
							.fillMaxSize()
							.padding(horizontal = 4.dp),
						verticalAlignment = Alignment.CenterVertically
					) {
						Box(
							modifier = Modifier
								.size(width = 48.dp, height = 64.dp)
								.pointerInput(step.id) {
									var draggedDistance = 0f

									detectDragGestures(
										onDragStart = {
											draggedDistance = 0f
										},
										onDragEnd = {
											draggedDistance = 0f
										},
										onDragCancel = {
											draggedDistance = 0f
										},
										onDrag = { change, dragAmount ->
											change.consume()
											draggedDistance += dragAmount.y

											val threshold =
												rowHeightPx.toFloat() + 8.dp.toPx()

											if (threshold <= 0f) {
												return@detectDragGestures
											}
											var index = steps.indexOfFirst {
												it.id == step.id
											}

											while (
												index >= 0 &&
												draggedDistance >= threshold &&
												index < steps.lastIndex
											) {
												val item = steps.removeAt(index)
												steps.add(index + 1, item)

												draggedDistance -= threshold
												index++
											}

											while (
												index > 0 &&
												draggedDistance <= -threshold
											) {
												val item = steps.removeAt(index)
												steps.add(index - 1, item)

												draggedDistance += threshold
												index--
											}
										}
									)
								},
							contentAlignment = Alignment.Center
						) {
							Text(text = "☰")
						}

						when (step) {
							is MacroStep.KeyDown -> {
								var expanded by remember {mutableStateOf(false)}
								val selectedKey = keys.firstOrNull {it.second == step.key}?.first ?: "Select a key"

								Box(modifier = Modifier.weight(1f)) {
									OutlinedButton(
										onClick = { expanded = true },
										modifier = Modifier.fillMaxWidth()
									) {
										Text("Key down: $selectedKey")
									}

									DropdownMenu(
										expanded = expanded,
										onDismissRequest = { expanded = false }
									) {
										keys.forEach { (label, value) ->
											DropdownMenuItem(
												text = { Text(label) },
												onClick = {
													val index = steps.indexOfFirst {
														it.id == step.id
													}

													if (index != -1) {
														steps[index] = step.copy(key = value)
													}

													expanded = false
												}
											)
										}
									}
								}
							}

							is MacroStep.KeyUp -> {
								var expanded by remember {mutableStateOf(false)}
								val selectedKey = keys.firstOrNull {it.second == step.key}?.first ?: "Select a key"

								Box(modifier = Modifier.weight(1f)) {
									OutlinedButton(
										onClick = { expanded = true },
										modifier = Modifier.fillMaxWidth()
									) {
										Text("Key up: $selectedKey")
									}

									DropdownMenu(
										expanded = expanded,
										onDismissRequest = { expanded = false }
									) {
										keys.forEach { (label, value) ->
											DropdownMenuItem(
												text = { Text(label) },
												onClick = {
													val index = steps.indexOfFirst {
														it.id == step.id
													}

													if (index != -1) {
														steps[index] = step.copy(key = value)
													}

													expanded = false
												}
											)
										}
									}
								}
							}

							is MacroStep.Delay -> {
								OutlinedTextField(
									value = step.milliseconds,
									onValueChange = { newValue ->
										val index = steps.indexOfFirst {
											it.id == step.id
										}

										if (index != -1) {
											steps[index] = step.copy(
												milliseconds = newValue
											)
										}
									},
									label = { Text("Delay (ms)") },
									placeholder = { Text("e.g. 300") },
									singleLine = true,
									keyboardOptions = KeyboardOptions(
										keyboardType = KeyboardType.Number
									),
									modifier = Modifier.weight(1f)
								)
							}
						}

						IconButton(
							onClick = {
								val index = steps.indexOfFirst {
									it.id == step.id
								}

								if (index != -1) {
									steps.removeAt(index)
								}
							}
						) {
							Text("✕")
						}
					}
				}
			}
		}

		Button(
			onClick = {
				val macro = SavedMacro(
					id = initialMacro?.id ?: UUID.randomUUID().toString(),
					name = macroName.trim(),
					steps = steps.toList()
				)

				onSave(macro)
			},
			enabled = macroName.isNotBlank() && steps.isNotEmpty() && allStepsValid,
			modifier = Modifier.fillMaxWidth()
		) {
			Text("Save macro")
		}

		if(initialMacro != null) {
			Button(
				onClick = {showDeleteConfirmation = true},
				modifier = Modifier.fillMaxWidth(),
				colors = ButtonDefaults.buttonColors(
					containerColor = Color.Red,
					contentColor = Color.Black
				)
			) {
				Text(text = "Delete macro")
			}
		}

		Button(
			onClick = onCancel,
			modifier = Modifier.fillMaxWidth()
		) {
			Text("Cancel")
		}
	}

	if(showDeleteConfirmation) {
		AlertDialog(
			onDismissRequest = {showDeleteConfirmation = false},
			title = {Text("Delete macro?")},
			text = {Text("Are you sure you want to delete this macro?")},
			dismissButton = {
				TextButton(
					onClick = {showDeleteConfirmation = false}
				) {
					Text("Cancel")
				}},
			confirmButton = {
				TextButton(
					onClick = {
						showDeleteConfirmation = false
						onDelete()
					}
				) {
					Text("Delete")
				}}
		)
	}

}