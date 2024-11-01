package com.dpudov.frames.datasource.local.mapper

import com.dpudov.domain.model.Instrument
import com.dpudov.frames.datasource.local.entity.InstrumentEntity
import com.dpudov.frames.datasource.local.entity.InstrumentEnum

fun InstrumentEntity.toData(): Instrument {
    return when (name) {
        Instrument.PENCIL_NAME -> Instrument.Pencil
        Instrument.BRUSH_NAME -> Instrument.Brush
        Instrument.ERASER_NAME -> Instrument.Eraser
        else -> Instrument.Pencil
    }
}

fun InstrumentEnum.toData(): Instrument = when (this) {
    InstrumentEnum.PENCIL -> Instrument.Pencil
    InstrumentEnum.BRUSH -> Instrument.Brush
    InstrumentEnum.ERASER -> Instrument.Eraser
}

fun Instrument.toEnum(): InstrumentEnum = when (this) {
    Instrument.Brush -> InstrumentEnum.BRUSH
    Instrument.Eraser -> InstrumentEnum.ERASER
    Instrument.Pencil -> InstrumentEnum.PENCIL
}