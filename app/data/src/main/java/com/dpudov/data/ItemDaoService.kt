package com.dpudov.data

import com.dpudov.domain.model.DrawableItem
import java.util.UUID

interface ItemDaoService {
    suspend fun getByFrame(frameId: UUID): List<DrawableItem>

    suspend fun add(item: DrawableItem)

    suspend fun addAll(items: List<DrawableItem>)

    suspend fun remove(item: DrawableItem)
}