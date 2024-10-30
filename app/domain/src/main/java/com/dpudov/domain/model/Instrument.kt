package com.dpudov.domain.model

sealed class Instrument {
    data object Pencil: Instrument()

    data object Brush: Instrument()

    data object Eraser: Instrument()
}