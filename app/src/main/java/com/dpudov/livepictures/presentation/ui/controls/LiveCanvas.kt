package com.dpudov.livepictures.presentation.ui.controls

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dpudov.domain.model.Frame
import com.dpudov.domain.model.Instrument
import com.dpudov.domain.model.Point
import com.dpudov.domain.model.Stroke
import com.dpudov.livepictures.R
import com.dpudov.livepictures.presentation.model.OnStrokeDrawn
import com.dpudov.livepictures.presentation.model.OnToolChanged
import java.util.UUID
import android.graphics.Color as StandardColor


@Composable
@Preview
fun LiveCanvas(
    frame: Frame? = null,
    strokes: List<Stroke> = emptyList(),
    instrument: Instrument = Instrument.Pencil,
    onStrokeDrawn: OnStrokeDrawn = OnStrokeDrawn { },
    onToolChanged: OnToolChanged = OnToolChanged { },
    color: Int = StandardColor.WHITE,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val backgroundBitmap = ImageBitmap.imageResource(context.resources, R.drawable.paper_texture)

    var currentStroke by remember { mutableStateOf<Stroke?>(null) }
    val strokePathCache = remember {
        Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888)
            .apply {
                eraseColor(android.graphics.Color.TRANSPARENT)
            }
    }
    LaunchedEffect(frame, strokes, currentStroke) {
        drawStrokesToBitmap(strokePathCache, strokes)
    }

    Box(modifier = modifier) {
        if (frame != null) {
            Canvas(modifier = Modifier
                .fillMaxSize()
                .pointerInput(frame, strokePathCache, instrument, color) {
                    detectDragGestures(
                        onDragStart = { offset: Offset ->
                            val id = UUID.randomUUID()
                            currentStroke = Stroke(
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
                                thickness = if (instrument == Instrument.Eraser) 20f else 10f,
                                instrument = instrument,
                                finishTimestamp = System.currentTimeMillis()
                            )
                        },
                        onDrag = { change: PointerInputChange, _ ->
                            currentStroke = currentStroke?.let {
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
                            currentStroke?.let {
                                onStrokeDrawn.onStrokeDrawn(it)
                            }
                            currentStroke = null
                        }
                    )
                }
            ) {
                drawImage(backgroundBitmap)

                drawIntoCanvas { canvas: Canvas ->
                    canvas.nativeCanvas.drawBitmap(
                        strokePathCache,
                        0f,
                        0f,
                        null
                    )
                }

                currentStroke?.let { stroke ->
                    val paint = Paint().apply {
                        this.color =
                            if (stroke.instrument == Instrument.Eraser) Color.Transparent
                            else Color(stroke.color)
                        blendMode =
                            if (stroke.instrument == Instrument.Eraser) BlendMode.Clear
                            else BlendMode.SrcOver
                        strokeWidth = stroke.thickness
                        style = PaintingStyle.Stroke
                        strokeCap = StrokeCap.Round
                    }

                    drawIntoCanvas { canvas ->
                        val path = Path().apply {
                            moveTo(stroke.points.first().x, stroke.points.first().y)
                            stroke.points.drop(1).forEach { lineTo(it.x, it.y) }
                        }
                        canvas.drawPath(path, paint)
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

fun DrawScope.drawStroke(stroke: Stroke) {
    if (stroke.points.isEmpty()) return
    val path = Path().apply {
        moveTo(stroke.points.first().x, stroke.points.first().y)
        for (point in stroke.points.drop(1)) {
            lineTo(point.x, point.y)
        }
    }

    drawPath(
        path = path,
        color = Color(stroke.color),
        style = androidx.compose.ui.graphics.drawscope.Stroke(
            width = stroke.thickness,
            cap = StrokeCap.Round
        ),
        blendMode = if (stroke.instrument == Instrument.Eraser) BlendMode.Clear
        else BlendMode.SrcOver
    )
}

fun drawStrokesToBitmap(bitmap: Bitmap, strokes: List<Stroke>) {
    val canvas = android.graphics.Canvas(bitmap)
    val paint = android.graphics.Paint()
    canvas.drawColor(android.graphics.Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

    strokes.forEach { stroke ->
        paint.apply {
            color =
                if (stroke.instrument == Instrument.Eraser) android.graphics.Color.TRANSPARENT
                else stroke.color
            xfermode =
                if (stroke.instrument == Instrument.Eraser) PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                else null
            style = android.graphics.Paint.Style.STROKE
            strokeCap = android.graphics.Paint.Cap.ROUND
            strokeWidth = stroke.thickness
        }

        val path = android.graphics.Path().apply {
            moveTo(stroke.points.first().x, stroke.points.first().y)
            stroke.points.drop(1).forEach { lineTo(it.x, it.y) }
        }
        canvas.drawPath(path, paint)
    }
}