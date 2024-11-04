package com.dpudov.data

import com.dpudov.domain.model.Circle
import com.dpudov.domain.model.DrawableItem
import com.dpudov.domain.model.Rect
import com.dpudov.domain.model.Stroke
import com.dpudov.domain.model.Triangle
import com.dpudov.domain.repository.IDrawableItemRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.util.UUID

class DrawableItemRepository(
    private val localStrokeDaoService: IStrokeDaoService,
    private val localCircleDaoService: ICircleDaoService,
    private val localTriangleDaoService: ITriangleDaoService,
    private val localRectangleDaoService: IRectangleDaoService
) : IDrawableItemRepository {
    override suspend fun getItemsByFrameId(frameId: UUID): List<DrawableItem> = coroutineScope {
        val deferredCircles = async {
            localCircleDaoService.getByFrame(frameId)
        }
        val deferredStrokes = async {
            localStrokeDaoService.getByFrame(frameId)
        }
        val deferredRects = async {
            localRectangleDaoService.getByFrame(frameId)
        }
        val deferredTriangles = async {
            localTriangleDaoService.getByFrame(frameId)
        }
        (deferredStrokes.await() + deferredCircles.await() + deferredRects.await() + deferredTriangles.await())
            .sortedBy(DrawableItem::finishTimestamp)
    }

    override suspend fun addItem(item: DrawableItem) {
        chooseLocalDaoService(item).add(item)
    }

    override suspend fun addAll(items: List<DrawableItem>) {
        if (items.isNotEmpty()) {
            val strokes = items.filterIsInstance<Stroke>()
            val circles = items.filterIsInstance<Circle>()
            val triangles = items.filterIsInstance<Triangle>()
            val rects = items.filterIsInstance<Rect>()
            localStrokeDaoService.addAll(strokes)
            localCircleDaoService.addAll(circles)
            localRectangleDaoService.addAll(rects)
            localTriangleDaoService.addAll(triangles)
        }
    }

    override suspend fun remove(item: DrawableItem) {
        chooseLocalDaoService(item).remove(item)
    }

    private fun chooseLocalDaoService(item: DrawableItem): ItemDaoService =
        when (item) {
            is Circle -> localCircleDaoService
            is Stroke -> localStrokeDaoService
            is Triangle -> localTriangleDaoService
            is Rect -> localRectangleDaoService
        }
}