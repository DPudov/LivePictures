package com.dpudov.domain.model

import java.util.UUID

data class Stroke(
    val id: UUID,

    val frameId: UUID,

    val color: Int,

    val thickness: Float,

    val instrument: Instrument,

    val finishTimestamp: Long,

    val points: List<Point>
) : DrawableItem
