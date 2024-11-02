package com.dpudov.exporter.exporter

import com.squareup.gifencoder.Image
import java.io.File

interface IGifExporter {
    fun start(outputFile: File)

    fun addImagesToGif(images: List<Image>, outputFile: File)

    fun finish(outputFile: File)

    companion object {
        const val DEFAULT_WIDTH = 240
        const val DEFAULT_HEIGHT = 426
    }
}