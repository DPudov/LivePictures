package com.dpudov.exporter.exporter

import com.squareup.gifencoder.Image
import java.io.File

interface IGifExporter {
    fun start(fps: Int, outputFile: File)

    fun addImagesToGif(images: List<Image>)

    fun finish()

    companion object {
        const val DEFAULT_WIDTH = 240
        const val DEFAULT_HEIGHT = 426
    }
}