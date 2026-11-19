package com.remote.phone.data.storage

import org.json.JSONArray
import org.json.JSONObject

private const val KEY_DOWN = "keyboard.down"
private const val KEY_UP = "keyboard.up"
private const val DELAY = "delay"

fun stepsToJson(steps: List<MacroStep>): JSONArray {
	val result = JSONArray()

	steps.forEach { step ->
		val json = when (step) {
			is MacroStep.KeyDown -> JSONObject().put("type", KEY_DOWN).put("payload", step.key)
			is MacroStep.KeyUp -> JSONObject().put("type", KEY_UP).put("payload", step.key)
			is MacroStep.Delay -> JSONObject().put("type", DELAY)
				.put("payload", JSONObject().put("milliseconds", step.milliseconds.toInt()))
		}
		result.put(json)
	}

	return result
}

fun stepsFromJson(array: JSONArray): List<MacroStep> {
	val steps = mutableListOf<MacroStep>()

	for (i in 0..<array.length()) {
		val json = array.getJSONObject(i)

		val step = when (val type = json.getString("type")) {
			KEY_DOWN -> MacroStep.KeyDown(key = json.getString("payload"))
			KEY_UP -> MacroStep.KeyUp(key = json.getString("payload"))
			DELAY -> MacroStep.Delay(milliseconds = json.getJSONObject("payload").getInt("milliseconds").toString())

			else -> throw IllegalArgumentException("What? How? $type")
		}

		steps.add(step)
	}
	return steps
}

fun macrosToJson(macros: List<SavedMacro>) : String {
	val array = JSONArray()

	macros.forEach {macro ->
		array.put(JSONObject()
			.put("id", macro.id)
			.put("name", macro.name)
			.put("steps", stepsToJson(macro.steps))
		)
	}

	return array.toString()
}

fun macrosFromJson(json: String): List<SavedMacro> {
	val array = JSONArray(json)
	val macros = mutableListOf<SavedMacro>()

	for (i in 0..<array.length()) {
		val item = array.getJSONObject(i)

		macros.add(
			SavedMacro(
				id = item.getString("id"),
				name = item.getString("name"),
				steps = stepsFromJson(item.getJSONArray("steps"))
			)
		)
	}
	return macros
}