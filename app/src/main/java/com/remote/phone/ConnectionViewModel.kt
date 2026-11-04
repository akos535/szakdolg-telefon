package com.remote.phone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remote.phone.datamodels.MediaInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import org.json.JSONObject

class ConnectionViewModel : ViewModel() {
	private val _status = MutableStateFlow("Disconnected")
	val status = _status.asStateFlow()

	val lang = "HU"

	private val _mediaInfo = MutableStateFlow<MediaInfo?>(null)
	val mediaInfo = _mediaInfo.asStateFlow()

	private val pcDiscovery = PcDiscovery()
	private val _discoveredPcs = MutableStateFlow<List<PcInfo>>(emptyList())
	val discoveredPcs = _discoveredPcs.asStateFlow()

	fun discoverPcs() {
		viewModelScope.launch {
			try {
				_discoveredPcs.value = pcDiscovery.discover()
			} catch (ex: IOException) {
				_status.value = "fail: ${ex.message}"
			}
		}
	}

	private val connection = ConnectPc(
		onStatus = { _status.value = it },
		onMessage = { text -> handleMessage(text) }
	)

	private val udpConnection = UdpConnection()

	fun connect(ip: String) {
		viewModelScope.launch {
			connection.connect(ip)
			udpConnection.connect(ip)
		}
	}

	fun disconnect() {
		connection.disconnect()
		udpConnection.close()
	}

	fun keypress(letter : String) {
		val message = JSONObject()
			.put("type", "keyboard.press")
			.put("payload", letter)
			.toString()

		connection.send(message)
	}

	fun moveMouse(dx: Float, dy: Float) {
		val message = JSONObject()
			.put("type", "mouse.move")
			.put("payload",
				JSONObject()
					.put("dx", dx.toDouble())
					.put("dy", dy.toDouble()))
			.toString()

		viewModelScope.launch {
			udpConnection.send(message)
		}
	}

	private fun handleMessage(text: String) {
		android.util.Log.d(
			"RemoteMedia",
			"Received ${text.length} characters: ${text.take(200)}"
		)
		try {
			val message = JSONObject(text)

			when(message.optString("type")) {
				"media.info" -> {
					val payload = message.optJSONObject("payload")

					if(payload == null) {
						_mediaInfo.value = null
						return
					}

					val thumbnail = if(payload.isNull("thumbnail")) {
						null
					} else {
						val bytes = android.util.Base64.decode(
							payload.getString("thumbnail"),
							android.util.Base64.DEFAULT
						)
						android.graphics.BitmapFactory.decodeByteArray(bytes,0,bytes.size)
					}

					_mediaInfo.value = MediaInfo(
						title = payload.optString("title"),
						artist = payload.optString("artist"),
						album = payload.optString("album"),
						sourceApp = payload.optString("sourceApp"),
						thumbnail = thumbnail
					)
				}
			}
		}
		catch (ex: Exception) {
			android.util.Log.e("Remote", "no media info read", ex)
		}
	}

	override fun onCleared() {
		connection.disconnect()
		super.onCleared()
	}
}