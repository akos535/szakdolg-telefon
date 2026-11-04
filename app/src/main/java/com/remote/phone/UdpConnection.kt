package com.remote.phone

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UdpConnection : Closeable {
	private var socket: DatagramSocket? = null
	private var serverAddress: InetAddress? = null

	suspend fun connect(pcIp: String) = withContext(Dispatchers.IO) {
		val address = InetAddress.getByName(pcIp)

		socket?.close()
		socket = DatagramSocket()
		serverAddress = address
	}

	suspend fun send(message: String): Boolean = withContext(Dispatchers.IO) {
		val currentSocket = socket
		val address = serverAddress

		if(currentSocket == null || address == null) {
			return@withContext false
		}

		val bytes = message.toByteArray(Charsets.UTF_8)

		try {
			currentSocket.send(DatagramPacket(bytes, bytes.size, address, 5001))

			true
		} catch (ex: java.io.IOException) {
			false
		}
	}

	override fun close() {
		socket?.close()
		socket = null
		serverAddress = null
	}
}