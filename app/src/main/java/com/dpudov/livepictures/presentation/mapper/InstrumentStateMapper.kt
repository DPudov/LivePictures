package com.dpudov.livepictures.presentation.mapper

import com.dpudov.domain.model.Instrument
import com.dpudov.livepictures.presentation.model.SelectedState

fun Instrument.toSelectedState(currentInstrument: Instrument): SelectedState =
    if (this == currentInstrument) SelectedState.Selected else SelectedState.Idle
