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
                centerX = 400f,
                centerY = 1000f
            ),
            Rect(
                id = UUID.randomUUID(),
                frameId = UUID.randomUUID(),
                finishTimestamp = System.currentTimeMillis(),
                color = 0,
                thickness = 10f,
                topLeftX = 400f,
                topLeftY = 400f,
                width = 100f,
                height = 100f
            ),
            Triangle(
                id = UUID.randomUUID(),
                frameId = UUID.randomUUID(),
                finishTimestamp = System.currentTimeMillis(),
                color = 0,
                thickness = 10f,
                x1 = 100f,
                y1 = 100f,
                x2 = 1000f,
                y2 = 1000f,
                x3 = 600f,
                y3 = 250f
            )
//            Stroke(
//                id = UUID.randomUUID(),
//                frameId = UUID.randomUUID(),
//                finishTimestamp = System.currentTimeMillis(),
//                color = 0,
//                thickness = 5f,
//                instrument = Instrument.Brush,
//                points = listOf(Point(
//                    id = 0L,
//                    strokeId = UUID.randomUUID()
//                ))
//            )
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
                        centerY = it.centerY + (random.nextFloat() * 50f) * random.nextInt(-1, 2)
                    )

                    is Rect -> it.copy(
                        id = UUID.randomUUID(),
                        frameId = nextFrame.id,
                        finishTimestamp = System.currentTimeMillis(),
                        color = random.nextInt(),
                        topLeftX = it.topLeftX + (random.nextFloat() * 400f) * random.nextInt(
                            -1,
                            2
                        ),
                        topLeftY = it.topLeftY + (random.nextFloat() * 400f) * random.nextInt(
                            -1,
                            2
                        ),
                        width = it.width + (random.nextFloat() * 400f) * random.nextInt(-1, 2),
                        height = it.height + (random.nextFloat() * 400f) * random.nextInt(-1, 2)
                    )

                    is Stroke -> it.copy(
                        id = UUID.randomUUID(),
                        frameId = nextFrame.id,
                        finishTimestamp = System.currentTimeMillis(),
                        color = random.nextInt()
                    )

                    is Triangle -> it.copy(
                        id = UUID.randomUUID(),
                        frameId = nextFrame.id,
                        finishTimestamp = System.currentTimeMillis(),
                        color = random.nextInt(),
                        x1 = it.x1 + (random.nextFloat() * 50f) * random.nextInt(-1, 2),
                        x2 = it.x2 + (random.nextFloat() * 50f) * random.nextInt(-1, 2),
                        x3 = it.x3 + (random.nextFloat() * 50f) * random.nextInt(-1, 2),
                        y1 = it.y1 + (random.nextFloat() * 50f) * random.nextInt(-1, 2),
                        y2 = it.y2 + (random.nextFloat() * 50f) * random.nextInt(-1, 2),
                        y3 = it.y3 + (random.nextFloat() * 50f) * random.nextInt(-1, 2),
                    )
                }
            }
            drawableItemRepository.addAll(newFigures)
            onNumberChanged(i + 1)
        }
    }
}