package com.remote.phone.data.storage

import android.content.Context
import android.util.AtomicFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MacroStorage(context: Context) {
	private val file = AtomicFile(File(context.applicationContext.filesDir, "macros.json"))

	suspend fun load(): List<SavedMacro> = withContext(Dispatchers.IO) {
		if(!file.baseFile.exists()) return@withContext emptyList()

		val bytes = file.readFully()
		val json = bytes.toString(Charsets.UTF_8)

		macrosFromJson(json)
	}

	suspend fun save(macros: List<SavedMacro>) {
		withContext(Dispatchers.IO) {
			val json = macrosToJson(macros)
			val bytes = json.toByteArray(Charsets.UTF_8)

			val stream = file.startWrite()

			try {
				stream.write(bytes)
				file.finishWrite(stream)
			} catch(ex: Exception) {
				file.failWrite(stream)
				throw ex
			}
		}
	}
}