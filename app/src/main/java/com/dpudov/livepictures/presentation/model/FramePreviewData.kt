package com.dpudov.livepictures.presentation.model

import android.graphics.Bitmap
import com.dpudov.domain.model.Frame

data class FramePreviewData(
    val frame: Frame,
    val bitmap: Bitmap
)