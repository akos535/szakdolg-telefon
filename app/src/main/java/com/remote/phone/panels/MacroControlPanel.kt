package com.remote.phone.panels

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remote.phone.ConnectionViewModel
import com.remote.phone.data.storage.MacroStorage
import com.remote.phone.data.storage.SavedMacro
import com.remote.phone.data.storage.stepsToJson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.json.JSONArray

@Composable
fun MacroControlPanel(
	connectionViewModel: ConnectionViewModel = viewModel(),
	onExecuteMacro: (JSONArray) -> Unit = { steps: JSONArray -> connectionViewModel.executeMacro(steps)}
) {
	val context = LocalContext.current
	val scope = rememberCoroutineScope()
	val storage = remember { MacroStorage(context.applicationContext) }
	var macros by remember { mutableStateOf<List<SavedMacro>>(emptyList()) }

	var isLoaded by remember { mutableStateOf(false) }
	var isSaving by remember { mutableStateOf(false) }
	var editMode by remember { mutableStateOf(false) }

	var editingMacro by remember { mutableStateOf<SavedMacro?>(null) }
	var editorOpen by remember { mutableStateOf(false) }

	var errorMessage by remember {mutableStateOf<String?>(null)}

	LaunchedEffect(storage) {
		try {
			macros = storage.load()
			isLoaded = true
		} catch (ex: CancellationException) {
			throw ex
		} catch (ex: Exception) {
			errorMessage = "Couldn't load macros: ${ex.message}"
		}
	}

	Column(
		modifier = Modifier.fillMaxSize().padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		errorMessage?.let { message ->
			Text(text = message, color = MaterialTheme.colorScheme.error)
		}

		if(editorOpen) {
			MacroEditor(
				initialMacro = editingMacro,
				onCancel = {
					if(!isSaving) {
						editorOpen = false
						editingMacro = null
					}
				},
				onSave = {savedMacro ->
					if(!isSaving) {
						isSaving = true

						val updatedMacros =
							if (macros.any {it.id == savedMacro.id}) {
								macros.map {existing ->
									if(existing.id == savedMacro.id) {
										savedMacro
									} else {
										existing
									}
								}
							} else {
								macros + savedMacro
							}
						scope.launch {
							try {
								storage.save(updatedMacros)

								macros = updatedMacros
								editorOpen = false
								editingMacro = null
								errorMessage = null
							} catch (ex: CancellationException) {
								throw ex
							} catch (ex: Exception) {
								errorMessage = "Couldn't save macro: ${ex.message}"
							} finally {
								isSaving = false
							}
						}
					}
				},
				onDelete = {
					val deletedMacro = editingMacro

					if(deletedMacro != null && !isSaving) {
						isSaving = true
						val updatedMacros = macros.filterNot {it.id == deletedMacro.id}

						scope.launch {
							try {
								storage.save(updatedMacros)

								macros = updatedMacros
								editingMacro = null
								editorOpen = false
								errorMessage = null
							} catch (ex: CancellationException) {
								throw ex
							} catch (ex: Exception) {
								errorMessage = "Couldn't delete macro: ${ex.message}"
							} finally {isSaving = false}
						}
					}
				}
			)
		} else {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Text("Edit mode")

				Switch(
					checked = editMode,
					onCheckedChange = {editMode = it},
					enabled = isLoaded
				)
			}
			Text(
				text =
					if(editMode) { "Tap a macro to edit it."}
					else { "Tap a macro to run it." }
			)

			if(!isLoaded && errorMessage == null) { Text("Loading macros...") }
			if(isLoaded && macros.isEmpty()) { Text("No macros saved yet.") }

			Column(
				modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				macros.forEach { macro ->
					Card(
						modifier = Modifier.fillMaxWidth().height(88.dp)
							.clickable{
								if(editMode) {
									editingMacro = macro
									editorOpen = true
								} else {
									connectionViewModel.executeMacro(stepsToJson(macro.steps))
								}
							}
					) {
						Row(
							modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
							verticalAlignment = Alignment.CenterVertically
						) {
							Text(macro.name)
						}
					}
				}
			}
			Button(
				onClick = {
					editingMacro = null
					editorOpen = true
				},
				enabled = isLoaded,
				modifier = Modifier.fillMaxWidth()
			) {
				Text("New macro")
			}
		}
	}
}