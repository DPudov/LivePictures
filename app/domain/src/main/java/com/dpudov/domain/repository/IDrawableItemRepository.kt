package com.dpudov.domain.repository

import com.dpudov.domain.model.DrawableItem
import java.util.UUID

interface IDrawableItemRepository {
    suspend fun getItemsByFrameId(frameId: UUID): List<DrawableItem>

    suspend fun addItem(item: DrawableItem)

    suspend fun addAll(items: List<DrawableItem>)

    suspend fun remove(item: DrawableItem)
}