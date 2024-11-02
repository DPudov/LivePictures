package com.dpudov.exporter.exporter

import com.squareup.gifencoder.Image
import java.io.File

interface IGifExporter {
    fun addImagesToGif(images: List<Image>, outputFile: File)

    fun finish(outputFile: File)

    companion object {
        const val DEFAULT_WIDTH = 1080
        const val DEFAULT_HEIGHT = 1920
    }
}