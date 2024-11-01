package com.dpudov.domain.model

sealed class Instrument(val name: String) {
    data object Pencil : Instrument(PENCIL_NAME)

    data object Brush : Instrument(BRUSH_NAME)

    data object Eraser : Instrument(ERASER_NAME)

    companion object {
        const val PENCIL_NAME = "pencil_instrument"
        const val BRUSH_NAME = "brush_instrument"
        const val ERASER_NAME = "eraser_instrument"
    }
}