package com.dpudov.exporter.exporter

import com.squareup.gifencoder.GifEncoder
import com.squareup.gifencoder.Image
import com.squareup.gifencoder.ImageOptions
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class GifExporter : IGifExporter {
    private val imageOptions = ImageOptions().apply {
        setDelay(1000L, TimeUnit.MILLISECONDS)
    }
    private var gifEncoder: GifEncoder? = null

    override fun start(fps: Int, outputFile: File) {
        if (fps >= 1) {
            val delay = 1000L / fps
            imageOptions.setDelay(delay, TimeUnit.MILLISECONDS)
        }
        val outputStream = FileOutputStream(outputFile)
        gifEncoder =
            GifEncoder(outputStream, IGifExporter.DEFAULT_WIDTH, IGifExporter.DEFAULT_HEIGHT, 0)
    }

    override fun addImagesToGif(images: List<Image>) {
        for (image in images) {
            gifEncoder?.addImage(image, imageOptions)
        }
    }

    override fun finish() {
        gifEncoder?.finishEncoding()
    }
}