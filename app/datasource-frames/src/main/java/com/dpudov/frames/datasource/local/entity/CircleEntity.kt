package com.dpudov.frames.datasource.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "circles",
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
data class CircleEntity(
    @PrimaryKey
    val id: UUID,
    val frameId: UUID,
    val finishTimestamp: Long,
    val color: Int,
    val thickness: Float,
    val radius: Float,
    val centerX: Float,
    val centerY: Float
)
