package com.remote.phone

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject

class ConnectionViewModel : ViewModel() {
	private val _status = MutableStateFlow("Disconnected")
	val status = _status.asStateFlow()

	private val _lastMessage = MutableStateFlow("")
	val lastMessage = _lastMessage.asStateFlow()

	val lang = "HU"

	private val connection = ConnectPc(
		onStatus = { _status.value = it },
		onMessage = { _lastMessage.value = it }
	)

	fun connect(ip: String) {
		connection.connect(ip)
	}

	fun playPause() {
		val message = JSONObject()
			.put("type", "keyboard.press")
			.put("payload", "MEDIA_PLAY_PAUSE")
			.toString()

		connection.send(message)
	}

	fun keypress(letter : String) {
		val message = JSONObject()
			.put("type", "keyboard.press")
			.put("payload", letter)
			.toString()

		connection.send(message)
	}

	fun sendTestMessage() {
		connection.send("hdfslkashfkjlf")
	}

	fun disconnect() {
		connection.disconnect()
	}

	override fun onCleared() {
		connection.disconnect()
		super.onCleared()
	}
}