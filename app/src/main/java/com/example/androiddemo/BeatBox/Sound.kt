package com.example.androiddemo.BeatBox

private const val WAV = ".wav"
class Sound(val assetPath:String) {
    val name = assetPath.split("/").last().removeSuffix(WAV)
}