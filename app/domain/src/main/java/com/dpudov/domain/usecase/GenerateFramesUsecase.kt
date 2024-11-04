package com.dpudov.domain.usecase

import com.dpudov.domain.model.Circle
import com.dpudov.domain.model.DrawableItem
import com.dpudov.domain.model.Frame
import com.dpudov.domain.model.Rect
import com.dpudov.domain.model.Stroke
import com.dpudov.domain.model.Triangle
import com.dpudov.domain.repository.IDrawableItemRepository
import com.dpudov.domain.repository.IFrameRepository
import java.util.UUID
import kotlin.random.Random

class GenerateFramesUsecase(
    private val frameRepository: IFrameRepository,
    private val drawableItemRepository: IDrawableItemRepository
) {
    suspend operator fun invoke(animationId: UUID, count: Int, onNumberChanged: (Int) -> Unit) {
        val initialFigures: List<DrawableItem> = listOf(
            Circle(
                id = UUID.randomUUID(),
                frameId = UUID.randomUUID(),
                finishTimestamp = System.currentTimeMillis(),
                color = 0,
                thickness = 10f,
                radius = 100f,
                centerX = 200f,
                centerY = 200f
            )
        )
        val random = Random.Default
        for (i in 0 until count) {
            val lastFrame = frameRepository.loadLastFrame(animationId)
            val nextFrame = Frame(
                id = UUID.randomUUID(),
                animationId = animationId,
                prevId = lastFrame?.id,
                nextId = null
            )
            frameRepository.addFrame(nextFrame)
            val newFigures: List<DrawableItem> = initialFigures.map {
                when (it) {
                    is Circle -> it.copy(
                        id = UUID.randomUUID(),
                        frameId = nextFrame.id,
                        finishTimestamp = System.currentTimeMillis(),
                        color = random.nextInt(),
                        radius = it.radius + (random.nextFloat() * 10f) * random.nextInt(-1, 2),
                        centerX = it.centerX + (random.nextFloat() * 50f) * random.nextInt(-1, 2),
                        centerY = it.centerX + (random.nextFloat() * 50f) * random.nextInt(-1, 2)
                    )

                    is Rect -> TODO()
                    is Stroke -> TODO()
                    is Triangle -> TODO()
                }
            }
            drawableItemRepository.addAll(newFigures)
            onNumberChanged(i + 1)
        }
    }
}