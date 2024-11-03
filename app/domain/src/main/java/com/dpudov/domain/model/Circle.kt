package com.dpudov.domain.model

import java.util.UUID

data class Circle(
    override val id: UUID,
    override val finishTimestamp: Long,
    override val color: Int,
    override val thickness: Float,
    val radius: Float,
    val centerX: Float,
    val centerY: Float,
) : DrawableItem