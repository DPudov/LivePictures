package com.dpudov.frames.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dpudov.frames.datasource.local.entity.InstrumentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InstrumentDao {
    @Query("select * from instruments")
    fun getAll(): Flow<List<InstrumentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInstrument(entity: InstrumentEntity)
}