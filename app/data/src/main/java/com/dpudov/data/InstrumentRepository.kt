package com.dpudov.data

import com.dpudov.domain.model.Instrument
import com.dpudov.domain.repository.IInstrumentRepository
import kotlinx.coroutines.flow.Flow

class InstrumentRepository(
    private val localDaoService: IInstrumentDaoService
) : IInstrumentRepository {
    override fun getAvailableInstruments(): Flow<List<Instrument>> =
        localDaoService.getAvailableInstruments()

    override suspend fun addInstrument(instrument: Instrument) {
        localDaoService.addInstrument(instrument)
    }
}