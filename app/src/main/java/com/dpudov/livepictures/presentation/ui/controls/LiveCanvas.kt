package com.dpudov.livepictures.presentation.ui.controls

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.ui.graphics.graphicsLayer
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
import com.dpudov.domain.model.Rect
import com.dpudov.domain.model.Stroke
import com.dpudov.domain.model.Triangle
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

    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

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
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
                .pointerInput(items, frame, instrument, color, size) {
                    if (animationState == AnimationState.Idle) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(0.5f, 5f)
                            offsetX += pan.x
                            offsetY += pan.y
                        }
                    }
                }
                .pointerInput(animationState, items, frame, instrument, color, size) {
                    if (animationState == AnimationState.Idle) {
                        if (instrument in Instrument.tappableInstruments) {
                            detectTapGestures(
                                onTap = { tapOffset ->
                                    currentItem = when (instrument) {
                                        Instrument.Figure.Circle -> Circle(
                                            id = UUID.randomUUID(),
                                            frameId = frame.id,
                                            finishTimestamp = System.currentTimeMillis(),
                                            thickness = size,
                                            color = color,
                                            centerX = tapOffset.x,
                                            centerY = tapOffset.y,
                                            radius = 50f * scale
                                        )

                                        Instrument.Figure.Rectangle -> Rect(
                                            id = UUID.randomUUID(),
                                            frameId = frame.id,
                                            finishTimestamp = System.currentTimeMillis(),
                                            thickness = size,
                                            color = color,
                                            topLeftX = tapOffset.x,
                                            topLeftY = tapOffset.y,
                                            width = 50f * scale,
                                            height = 50f * scale
                                        )

                                        Instrument.Figure.Triangle -> Triangle(
                                            id = UUID.randomUUID(),
                                            frameId = frame.id,
                                            finishTimestamp = System.currentTimeMillis(),
                                            thickness = size,
                                            color = color,
                                            x1 = tapOffset.x,
                                            y1 = tapOffset.y,
                                            x2 = tapOffset.x + 50f * scale,
                                            y2 = tapOffset.y + 50f * scale,
                                            x3 = tapOffset.x - 50f * scale,
                                            y3 = tapOffset.y + 50f * scale,
                                        )

                                        else -> null
                                    }
                                    currentItem?.let {
                                        onItemDrawn.onItemDrawn(it)
                                    }
                                }
                            )
                        }
                    }
                }
                .pointerInput(animationState, items, frame, instrument, color, size) {
                    if (animationState == AnimationState.Idle) {
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
        is Rect -> drawRectangle(item)
        is Triangle -> drawTriangle(item)
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

fun android.graphics.Canvas.drawRectangle(rect: Rect) {
    val paint = android.graphics.Paint().apply {
        isAntiAlias = true
        this.color = rect.color
        xfermode = null
        strokeWidth = rect.thickness
        style = android.graphics.Paint.Style.STROKE
        strokeCap = android.graphics.Paint.Cap.ROUND
        clearShadowLayer()
    }

    drawRect(
        /* left = */ rect.topLeftX,
        /* top = */ rect.topLeftY,
        /* right = */ rect.topLeftX + rect.width,
        /* bottom = */ rect.topLeftY + rect.height,
        /* paint = */ paint
    )
}

fun android.graphics.Canvas.drawTriangle(triangle: Triangle) {
    val paint = android.graphics.Paint().apply {
        isAntiAlias = true
        this.color = triangle.color
        xfermode = null
        strokeWidth = triangle.thickness
        style = android.graphics.Paint.Style.STROKE
        strokeCap = android.graphics.Paint.Cap.ROUND
        clearShadowLayer()
    }
    val path = android.graphics.Path().apply {
        moveTo(triangle.x1, triangle.y1)
        lineTo(triangle.x2, triangle.y2)
        lineTo(triangle.x3, triangle.y3)
        lineTo(triangle.x1, triangle.y1)
    }
    drawPath(
        /* path = */ path,
        /* paint = */ paint
    )
}
