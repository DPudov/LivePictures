package com.dpudov.domain.repository

import com.dpudov.domain.model.Instrument
import kotlinx.coroutines.flow.Flow

interface IInstrumentRepository {
    fun getAvailableInstruments(): Flow<List<Instrument>>

    suspend fun addInstrument(instrument: Instrument)
}