package com.remote.phone

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ConnectPc(
	private val onStatus: (String) -> Unit,
	private val onMessage: (String) -> Unit
) {
	private val client = OkHttpClient()
	private var socket: WebSocket? = null

	fun connect(ip: String, port: Int = 5000) {
		socket?.cancel()

		val request = Request.Builder()
			.url("ws://$ip:$port/ws")
			.build()

		onStatus("Connecting")

		socket = client.newWebSocket(
			request,
			object : WebSocketListener() {
				override fun onOpen(
					webSocket: WebSocket,
					response: Response
				) {
					onStatus("Connected")
				}

				override fun onMessage(
					webSocket: WebSocket,
					text: String
				) {
					onMessage(text)
				}

				override fun onClosing(
					webSocket: WebSocket,
					code: Int,
					reason: String
				) {
					webSocket.close(code, reason)
				}

				override fun onClosed(
					webSocket: WebSocket,
					code: Int,
					reason: String
				) {
					onStatus("Disconnected")
				}

				override fun onFailure(
					webSocket: WebSocket,
					t: Throwable,
					response: Response?
				) {
					onStatus("Connection failed: ${t.message}")
				}
			}
		)
	}

	fun send(message: String): Boolean {
		return socket?.send(message) ?: false
	}

	fun disconnect() {
		socket?.close(1000, "Disconnected by user")
		socket = null
	}
}