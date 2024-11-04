package com.dpudov.domain.model

import java.util.UUID

data class Stroke(
    override val id: UUID,

    override val frameId: UUID,

    override val color: Int,

    override val thickness: Float,

    val instrument: Instrument,

    override val finishTimestamp: Long,

    val points: List<Point>
) : DrawableItem
