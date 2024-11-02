package com.dpudov.exporter.repository

import android.graphics.Bitmap
import java.io.File

interface IGifExportRepository {
    suspend fun createGif(images: List<Bitmap>, outputFile: File)
}