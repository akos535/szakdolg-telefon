package com.remote.phone.datamodels

data class MediaInfo(
	val title: String,
	val artist: String,
	val album: String,
	val sourceApp: String,
	val thumbnail: android.graphics.Bitmap?
)