package com.dpudov.livepictures.presentation.ui.controls

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import com.dpudov.domain.model.Circle
import com.dpudov.domain.model.DrawableItem
import com.dpudov.domain.model.Frame
import com.dpudov.domain.model.Instrument
import com.dpudov.domain.model.Point
import com.dpudov.domain.model.Stroke
import com.dpudov.livepictures.R
import com.dpudov.livepictures.presentation.model.AnimationState
import com.dpudov.livepictures.presentation.model.OnItemDrawn
import com.dpudov.livepictures.presentation.model.OnToolChanged
import java.util.UUID
import android.graphics.Color as StandardColor


@Composable
@Preview
fun LiveCanvas(
    frame: Frame? = null,
    previousItems: List<DrawableItem> = emptyList(),
    items: List<DrawableItem> = emptyList(),
    instrument: Instrument = Instrument.Pencil,
    animationState: AnimationState = AnimationState.Idle,
    onItemDrawn: OnItemDrawn = OnItemDrawn { },
    onToolChanged: OnToolChanged = OnToolChanged { },
    color: Int = StandardColor.WHITE,
    size: Float = 1f,
    modifier: Modifier = Modifier
) {
    var currentItem by remember { mutableStateOf<DrawableItem?>(null) }

    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    val transparentBitmap = remember(canvasSize) {
        if (canvasSize.width > 0 && canvasSize.height > 0) {
            Bitmap.createBitmap(canvasSize.width, canvasSize.height, Bitmap.Config.ARGB_8888)
                .apply {
                    eraseColor(android.graphics.Color.TRANSPARENT)
                }
        } else {
            null
        }
    }

    Box(modifier = modifier) {
        if (frame != null) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.paper_texture),
                contentDescription = stringResource(R.string.canvas_background)
            )
            Canvas(modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { size ->
                    canvasSize = size
                }
//                .pointerInput(items, frame, instrument, color, size) {
//                    if (animationState == AnimationState.Idle) {
//                        detectTransformGestures(
//                            onGesture = { centroid, pan, zoom, rotation ->
//                                if (instrument in Instrument.tappableInstruments && currentItem != null) {
//                                    currentItem = currentItem?.let {
//                                        when (it) {
//                                            is Circle -> it.copy(
//                                                radius = it.radius * zoom,
//                                                centerX = it.centerX + pan.x,
//                                                centerY = it.centerY + pan.y
//                                            )
//
//                                            is Stroke -> null // do nothing
//                                        }
//                                    }
//                                }
//                            })
//                    }
//                }
                .pointerInput(items, frame, instrument, color, size) {
                    if (animationState == AnimationState.Idle) {
//                        if (instrument in Instrument.tappableInstruments) {
//                            detectTapGestures(
//                                onPress = { offset: Offset ->
//                                    currentItem = when (instrument) {
//                                        Instrument.Figure.Circle -> Circle(
//                                            id = UUID.randomUUID(),
//                                            frameId = frame.id,
//                                            finishTimestamp = System.currentTimeMillis(),
//                                            color = color,
//                                            thickness = size,
//                                            radius = Circle.DEFAULT_RADIUS,
//                                            centerX = offset.x,
//                                            centerY = offset.y
//                                        )
//
//                                        Instrument.Figure.Rectangle -> TODO()
//                                        Instrument.Figure.Triangle -> TODO()
//                                        else -> null
//                                    }
//                                },
//                                onTap = {
//                                    currentItem?.let {
//                                        onItemDrawn.onItemDrawn(it)
//                                    }
//                                    currentItem = null
//                                }
//                            )
//                            awaitPointerEventScope {
//                                while (true) {
//                                    val event = awaitPointerEvent()
//                                    val position = event.changes.first().position
//
//                                    if (event.changes.first().pressed) {
//                                        currentItem = when (instrument) {
//                                            Instrument.Figure.Circle -> Circle(
//                                                id = UUID.randomUUID(),
//                                                frameId = frame.id,
//                                                finishTimestamp = System.currentTimeMillis(),
//                                                color = color,
//                                                thickness = size,
//                                                radius = Circle.DEFAULT_RADIUS,
//                                                centerX = position.x,
//                                                centerY = position.y
//                                            )
//
//                                            Instrument.Figure.Rectangle -> TODO()
//                                            Instrument.Figure.Triangle -> TODO()
//                                            else -> null
//                                        }
//                                    }
//                                    var initialDistance = 0f
//                                    var initialAngle = 0f
//                                    while (event.changes.first().pressed) {
//                                        val transformEvent = awaitPointerEvent()
//                                        val pan = transformEvent.changes.first().positionChange()
//                                        if (transformEvent.changes.size >= 2) {
//                                            val pointer1 = transformEvent.changes[0].position
//                                            val pointer2 = transformEvent.changes[1].position
//
//                                            val dx = pointer2.x - pointer1.x
//                                            val dy = pointer2.y - pointer1.y
//                                            val distance = kotlin.math.sqrt(dx * dx + dy * dy)
//                                            val angle = kotlin.math.atan2(dy, dx)
//
//                                            if (initialDistance == 0f) {
//                                                initialDistance = distance
//                                                initialAngle = angle
//                                            }
//                                            val zoom = distance / initialDistance
//                                            val rotation = (angle - initialAngle) * (180 / Math.PI).toFloat()
//                                            currentItem = currentItem?.let {
//                                                when (it) {
//                                                    is Circle -> it.copy(
//                                                        radius = it.radius * zoom,
//                                                        centerX = it.centerX + pan.x,
//                                                        centerY = it.centerY + pan.y
//                                                    )
//
//                                                    is Stroke -> null // do nothing
//                                                }
//                                            }
//                                        } else {
//                                            currentItem = currentItem?.let {
//                                                when (it) {
//                                                    is Circle -> it.copy(
//                                                        radius = it.radius,
//                                                        centerX = it.centerX + pan.x,
//                                                        centerY = it.centerY + pan.y
//                                                    )
//
//                                                    is Stroke -> null // do nothing
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                    if (!event.changes.first().pressed) {
//                                        currentItem?.let {
//                                            onItemDrawn.onItemDrawn(it)
//                                        }
//                                        currentItem = null
//                                    }
//                                }
//                            }
//                        }

                        if (instrument is Instrument.Pencil || instrument in Instrument.draggableInstruments) {
                            detectDragGestures(
                                onDragStart = { offset: Offset ->
                                    val id = UUID.randomUUID()
                                    currentItem = Stroke(
                                        id = id,
                                        frameId = frame.id,
                                        points = listOf(
                                            Point(
                                                id = 0L,
                                                strokeId = id,
                                                x = offset.x,
                                                y = offset.y
                                            )
                                        ),
                                        color = if (instrument == Instrument.Eraser) Color.Transparent.toArgb() else color,
                                        thickness = if (instrument == Instrument.Pencil) Instrument.PENCIL_SIZE else size,
                                        instrument = instrument,
                                        finishTimestamp = System.currentTimeMillis()
                                    )
                                },
                                onDrag = { change: PointerInputChange, _ ->
                                    currentItem = (currentItem as? Stroke)?.let {
                                        it.copy(
                                            points = it.points +
                                                    Point(
                                                        id = 0L,
                                                        strokeId = it.id,
                                                        x = change.position.x,
                                                        y = change.position.y
                                                    )
                                        )
                                    }
                                },
                                onDragEnd = {
                                    currentItem?.let {
                                        onItemDrawn.onItemDrawn(it)
                                    }
                                    currentItem = null
                                }
                            )
                        }
                    }
                }
            ) {
                transparentBitmap?.let { bitmap: Bitmap ->
                    val canvas = android.graphics.Canvas(bitmap)
                    canvas.drawColor(android.graphics.Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                    if (animationState == AnimationState.Idle) {
                        previousItems.forEach {
                            canvas.drawItem(it)
                        }
                    }
                    items.forEach {
                        canvas.drawItem(it)
                    }
                    currentItem?.let {
                        canvas.drawItem(it)
                    }
                    drawIntoCanvas {
                        it.nativeCanvas.drawBitmap(bitmap, 0f, 0f, null)
                    }
                }
            }
        } else {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.no_frame_exist_add_one_to_start_drawing)
            )
        }
    }
}

fun android.graphics.Canvas.drawItem(item: DrawableItem) {
    when (item) {
        is Circle -> drawCircle(item)
        is Stroke -> drawStroke(item)
    }
}

fun android.graphics.Canvas.drawStroke(stroke: Stroke) {
    if (stroke.points.isEmpty()) return
    val initialPoint = stroke.points.first()
    val path = android.graphics.Path().apply {
        moveTo(initialPoint.x, initialPoint.y)
        for (point in stroke.points.drop(1)) {
            lineTo(point.x, point.y)
        }
    }
    val paint = android.graphics.Paint().apply {
        isAntiAlias = true
        this.color =
            if (stroke.instrument == Instrument.Eraser) android.graphics.Color.TRANSPARENT
            else stroke.color
        xfermode =
            if (stroke.instrument == Instrument.Eraser) PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            else null
        strokeWidth = stroke.thickness
        style = android.graphics.Paint.Style.STROKE
        strokeCap = android.graphics.Paint.Cap.ROUND
        clearShadowLayer()
    }

    drawPath(
        /* path = */ path,
        /* paint = */ paint
    )
}

fun android.graphics.Canvas.drawCircle(circle: Circle) {
    val paint = android.graphics.Paint().apply {
        isAntiAlias = true
        this.color = circle.color
        xfermode = null
        strokeWidth = circle.thickness
        style = android.graphics.Paint.Style.STROKE
        strokeCap = android.graphics.Paint.Cap.ROUND
        clearShadowLayer()
    }

    drawCircle(
        /* cx = */ circle.centerX,
        /* cy = */ circle.centerY,
        /* radius = */ circle.radius,
        /* paint = */ paint
    )
}
