package com.remote.phone.data.storage

import android.graphics.Bitmap

data class MediaInfo(
	val title: String,
	val artist: String,
	val album: String,
	val sourceApp: String,
	val thumbnail: Bitmap?
)