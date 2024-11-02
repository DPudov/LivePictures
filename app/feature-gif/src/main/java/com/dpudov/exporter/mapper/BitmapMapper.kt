package com.dpudov.exporter.mapper

import android.graphics.Bitmap
import com.squareup.gifencoder.Image

fun Bitmap.toGifFrame(): Image {
    val width = width
    val height = height
    val rgbMatrix = Array(height) { IntArray(width) }
    for (y in 0 until height) {
        for (x in 0 until width) {
            rgbMatrix[y][x] = getPixel(x, y) and 0x00FFFFFF
        }
    }
    return Image.fromRgb(rgbMatrix)
}