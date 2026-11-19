package com.remote.phone

import android.os.SystemClock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import java.util.UUID

data class PcInfo(
	val name: String,
	val ip: String,
	val webSocketPort: Int,
	val udpPort: Int
)

class PcDiscovery {
	suspend fun discover(): List<PcInfo> = withContext(Dispatchers.IO) {
		val requestId = UUID.randomUUID().toString()

		val request = JSONObject()
			.put("type", "discovery.request")
			.put("requestId", requestId)
			.toString()
			.toByteArray(Charsets.UTF_8)

		val computers = mutableMapOf<String, PcInfo>()

		DatagramSocket().use {socket ->
			socket.broadcast = true
			val broadcastAddress = InetAddress.getByName("255.255.255.255")

			val requestPacket = DatagramPacket(request, request.size,broadcastAddress, 5001)

			socket.send(requestPacket)

			val deadline = SystemClock.elapsedRealtime() + 2000

			while(true) {
				currentCoroutineContext().ensureActive()
				val remaining = deadline - SystemClock.elapsedRealtime()
				if(remaining <= 0) break

				socket.soTimeout = minOf(remaining, 250L).toInt()

				val buffer = ByteArray(4096)
				val responsePakcet = DatagramPacket(buffer, buffer.size)

				try {
					socket.receive(responsePakcet)
				} catch (_: SocketTimeoutException) {
					continue
				}

				val json = String(responsePakcet.data, responsePakcet.offset, responsePakcet.length, Charsets.UTF_8)
				try {
					val response = JSONObject(json)

					if (response.optString("type") != "discovery.response")
						continue

					if (response.optString("requestId") != requestId)
						continue

					val ip = responsePakcet.address.hostAddress ?: continue
					val name = response.optString("name")
					val webSocketPort = response.optInt("webSocketPort", 0)
					val udpPort = response.optInt("udpPort", 0)

					if (name.isBlank() ||
						webSocketPort !in 1..65535 ||
						udpPort !in 1..65535
					) {
						continue
					}

					val pc = PcInfo(
						name = name,
						ip = ip,
						webSocketPort = webSocketPort,
						udpPort = udpPort
					)
					computers["$ip:$webSocketPort"] = pc
				} catch (_: JSONException) { }
			}
		}
		computers.values.toList()
	}
}