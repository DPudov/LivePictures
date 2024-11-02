package com.dpudov.exporter.repository

import android.graphics.Bitmap
import com.dpudov.exporter.exporter.IGifExporter
import com.dpudov.exporter.mapper.toGifFrame
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class GifRepository(
    private val gifExporter: IGifExporter,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IGifExportRepository {
    override suspend fun start(outputFile: File) {
        withContext(dispatcher) {
            gifExporter.start(outputFile)
        }
    }

    override suspend fun addImages(images: List<Bitmap>, outputFile: File) {
        withContext(dispatcher) {
            val rgbImages = images.map(Bitmap::toGifFrame)
            gifExporter.addImagesToGif(rgbImages, outputFile)
        }
    }

    override suspend fun finish(outputFile: File) {
        withContext(dispatcher) {
            gifExporter.finish(outputFile)
        }
    }
}