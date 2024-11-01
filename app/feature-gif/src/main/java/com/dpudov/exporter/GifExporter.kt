package com.dpudov.exporter

import com.squareup.gifencoder.GifEncoder
import java.io.File
import java.io.FileOutputStream

class GifExporter {
    fun createGifFromImages(images: List<File>, outputFile: File) {
        val outputStream = FileOutputStream(outputFile)
        val gifEncoder = GifEncoder(outputStream, 256, 256, 0)

        for (image in images) {
//            gifEncoder.addImage(BitmapFactory.decodeFile(image.absolutePath).)
        }

        gifEncoder.finishEncoding()
        outputStream.close()
    }
}