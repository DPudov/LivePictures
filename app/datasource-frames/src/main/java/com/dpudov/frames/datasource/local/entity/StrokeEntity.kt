package com.dpudov.frames.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "strokes")
data class StrokeEntity(
    @PrimaryKey
    val id: UUID,

    val frameId: UUID,

    val color: Int,

    val thickness: Float,

    val instrument: InstrumentEnum,

    val finishTimestamp: Long
)