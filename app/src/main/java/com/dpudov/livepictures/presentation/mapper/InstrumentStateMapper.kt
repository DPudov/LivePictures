package com.dpudov.livepictures.presentation.mapper

import com.dpudov.domain.model.Instrument
import com.dpudov.livepictures.presentation.model.SelectedState
import com.dpudov.livepictures.presentation.model.Tool

fun Instrument.toSelectedState(currentInstrument: Instrument): SelectedState =
    if (this == currentInstrument) SelectedState.Selected else SelectedState.Idle

fun Tool.toData(): Instrument = when (this) {
    Tool.ERASER -> Instrument.Eraser
    Tool.BRUSH -> Instrument.Brush
    Tool.PENCIL -> Instrument.Pencil
}