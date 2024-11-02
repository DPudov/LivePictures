package com.dpudov.frames.datasource.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "strokes",
    foreignKeys = [
        ForeignKey(
            entity = FrameEntity::class,
            parentColumns = ["id"],
            childColumns = ["frameId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["frameId"])
    ]
)
data class StrokeEntity(
    @PrimaryKey
    val id: UUID,

    val frameId: UUID,

    val color: Int,

    val thickness: Float,

    val instrument: InstrumentEnum,

    val finishTimestamp: Long
)