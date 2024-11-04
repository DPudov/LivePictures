package com.dpudov.domain.model

sealed class Instrument(val name: String) {
    data object Pencil : Instrument(PENCIL_NAME)

    data object Brush : Instrument(BRUSH_NAME)

    data object Eraser : Instrument(ERASER_NAME)

    sealed class Figure : Instrument(FIGURE_INSTRUMENT) {
        data object Rectangle : Figure()

        data object Triangle : Figure()

        data object Circle : Figure()
    }

    companion object {
        const val PENCIL_SIZE = 2f
        const val MAX_SIZE = 32f
        const val PENCIL_NAME = "pencil_instrument"
        const val BRUSH_NAME = "brush_instrument"
        const val ERASER_NAME = "eraser_instrument"
        const val FIGURE_INSTRUMENT = "figure_instrument"

        val tappableInstruments = setOf(
            Figure.Rectangle, Figure.Triangle, Figure.Circle
        )

        val draggableInstruments = setOf(
            Brush, Eraser, Pencil
        )
    }
}