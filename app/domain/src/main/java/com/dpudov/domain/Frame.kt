package com.dpudov.domain

import java.util.UUID

data class Frame(
    val id: UUID,
    val animationId: UUID,
    val prevId: UUID?,
    val nextId: UUID?
)