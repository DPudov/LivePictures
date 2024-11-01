package com.dpudov.livepictures.presentation.model

import com.dpudov.domain.model.Stroke

fun interface OnStrokeDrawn {
    fun onStrokeDrawn(stroke: Stroke)
}