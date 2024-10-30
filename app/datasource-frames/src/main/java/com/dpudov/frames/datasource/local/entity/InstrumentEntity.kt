package com.dpudov.frames.datasource.local.entity

import androidx.room.Entity
import java.util.UUID

@Entity(tableName = "instruments")
data class InstrumentEntity(
    val id: UUID,

    val name: String
)
