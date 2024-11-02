package com.dpudov.exporter.exporter

import com.squareup.gifencoder.GifEncoder
import com.squareup.gifencoder.Image
import com.squareup.gifencoder.ImageOptions
import java.io.File
import java.io.FileOutputStream

class GifExporter : IGifExporter {
    private val imageOptions = ImageOptions()
    private var gifEncoder: GifEncoder? = null

    override fun addImagesToGif(images: List<Image>, outputFile: File) {
        val outputStream = FileOutputStream(outputFile, true)
        gifEncoder =
            GifEncoder(outputStream, IGifExporter.DEFAULT_WIDTH, IGifExporter.DEFAULT_HEIGHT, 0)

        for (image in images) {
            gifEncoder?.addImage(image, imageOptions)
        }
    }

    override fun finish(outputFile: File) {
        gifEncoder?.finishEncoding()
    }
}