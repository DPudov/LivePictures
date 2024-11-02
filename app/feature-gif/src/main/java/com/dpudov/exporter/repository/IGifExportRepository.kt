package com.dpudov.exporter.repository

import android.graphics.Bitmap
import java.io.File

interface IGifExportRepository {
    suspend fun addImages(images: List<Bitmap>, outputFile: File)

    suspend fun finish(outputFile: File)
}