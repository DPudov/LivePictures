package com.dpudov.exporter.exporter

import com.squareup.gifencoder.Image
import java.io.File

interface IGifExporter {
    fun createGifFromImages(images: List<Image>, outputFile: File)

    companion object {
        const val DEFAULT_WIDTH = 256
        const val DEFAULT_HEIGHT = 256
    }
}