package com.dpudov.frames.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "frames")
data class FrameEntity(
    @PrimaryKey
    val id: UUID,

    val animationId: UUID,

    val prevFrameId: UUID?,

    val nextFrameId: UUID?
)
