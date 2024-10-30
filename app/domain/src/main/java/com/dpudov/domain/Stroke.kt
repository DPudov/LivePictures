package com.dpudov.domain

import java.util.UUID

data class Stroke(
    val id: UUID,

    val frameId: UUID,

    val color: Int,

    val thickness: Float,

    val instrumentId: UUID,

    val finishTimestamp: Long
) : DrawableItem
