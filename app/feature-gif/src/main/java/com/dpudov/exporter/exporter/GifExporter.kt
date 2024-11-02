package com.dpudov.exporter.exporter

import com.squareup.gifencoder.GifEncoder
import com.squareup.gifencoder.Image
import com.squareup.gifencoder.ImageOptions
import java.io.File
import java.io.FileOutputStream

class GifExporter : IGifExporter {
    override fun createGifFromImages(images: List<Image>, outputFile: File) {
        val outputStream = FileOutputStream(outputFile)
        outputStream.use {
            val gifEncoder =
                GifEncoder(outputStream, IGifExporter.DEFAULT_WIDTH, IGifExporter.DEFAULT_HEIGHT, 0)
            val imageOptions = ImageOptions()

            for (image in images) {
                gifEncoder.addImage(image, imageOptions)
            }

            gifEncoder.finishEncoding()
        }
    }
}