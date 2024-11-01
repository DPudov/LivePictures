package com.dpudov.frames.datasource.local.service

import com.dpudov.data.IInstrumentDaoService
import com.dpudov.domain.model.Instrument
import com.dpudov.frames.datasource.local.dao.InstrumentDao
import com.dpudov.frames.datasource.local.database.AppDatabase
import com.dpudov.frames.datasource.local.entity.InstrumentEntity
import com.dpudov.frames.datasource.local.mapper.toData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

class InstrumentDaoService(
    appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IInstrumentDaoService {
    private val instrumentDao: InstrumentDao = appDatabase.instrumentDao()
    override fun getAvailableInstruments(): Flow<List<Instrument>> =
        instrumentDao.getAll()
            .map { entities -> entities.map(InstrumentEntity::toData) }
            .flowOn(dispatcher)

    override suspend fun addInstrument(instrument: Instrument) {
        withContext(dispatcher) {
            val currentInstruments = instrumentDao.getAll().firstOrNull()
            val hasInstrument =
                currentInstruments?.any { entity -> entity.name == instrument.name } ?: false
            if (!hasInstrument) {
                val id = UUID.randomUUID()
                val entity = InstrumentEntity(
                    id = id,
                    name = instrument.name
                )
                instrumentDao.addInstrument(entity)
            }
        }
    }
}