package com.dpudov.data

import com.dpudov.domain.model.Instrument
import kotlinx.coroutines.flow.Flow

interface IInstrumentDaoService {
    fun getAvailableInstruments(): Flow<List<Instrument>>

    suspend fun addInstrument(instrument: Instrument)
}