package com.dpudov.frames.datasource.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class StrokeWithPoints(
    @Embedded
    val stroke: StrokeEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "stroke_id"
    )
    val points: List<PointEntity>
)