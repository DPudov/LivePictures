package com.dpudov.domain.model

import java.util.UUID

data class Triangle(
    override val id: UUID,
    override val frameId: UUID,
    override val finishTimestamp: Long,
    override val color: Int,
    override val thickness: Float,
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float,
    val x3: Float,
    val y3: Float
) : DrawableItem