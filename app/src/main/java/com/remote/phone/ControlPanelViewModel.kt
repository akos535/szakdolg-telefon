package com.remote.phone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ControlPanelViewModel: ViewModel() {
	var selectedMenuOption by mutableStateOf(ControlPanels.MEDIA)

	fun selectMenuOption(option: ControlPanels) {
		selectedMenuOption = option
	}
}