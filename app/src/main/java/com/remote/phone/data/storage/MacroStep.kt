package com.remote.phone.data.storage

import java.util.UUID

sealed interface MacroStep {
	val id: String

	data class KeyDown(
		override val id: String = UUID.randomUUID().toString(),
		val key: String = ""
	): MacroStep

	data class KeyUp(
		override val id: String = UUID.randomUUID().toString(),
		val key: String = ""
	): MacroStep

	data class Delay(
		override val id: String = UUID.randomUUID().toString(),
		val milliseconds: String = ""
	): MacroStep
}

data class SavedMacro(
	val id: String = UUID.randomUUID().toString(),
	val name: String,
	val steps: List<MacroStep>
)