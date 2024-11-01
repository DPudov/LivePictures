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
//    val context = LocalContext.current
//    val backgroundBitmap = ImageBitmap.imageResource(context.resources, R.drawable.paper_texture)

    var currentStroke by remember { mutableStateOf<Stroke?>(null) }

    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    val transparentBitmap = remember(canvasSize) {
        if (canvasSize.width > 0 && canvasSize.height > 0) {
            Bitmap.createBitmap(canvasSize.width, canvasSize.height, Bitmap.Config.ARGB_8888).apply {
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
                .pointerInput(frame, strokes, instrument, color) {
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
                transparentBitmap?.let { bitmap: Bitmap ->
                    val canvas = android.graphics.Canvas(bitmap)
                    canvas.drawColor(android.graphics.Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                    strokes.forEach {
                        canvas.drawStroke(it)
                    }
                    currentStroke?.let {
                        canvas.drawStroke(it)
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

fun android.graphics.Canvas.drawStroke(stroke: Stroke) {
    if (stroke.points.isEmpty()) return
    val path = android.graphics.Path().apply {
        moveTo(stroke.points.first().x, stroke.points.first().y)
        for (point in stroke.points.drop(1)) {
            lineTo(point.x, point.y)
        }
    }
    val paint = android.graphics.Paint().apply {
        this.color =
            if (stroke.instrument == Instrument.Eraser) android.graphics.Color.TRANSPARENT
            else stroke.color
        xfermode =
            if (stroke.instrument == Instrument.Eraser) PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            else null
        strokeWidth = stroke.thickness
        style = android.graphics.Paint.Style.STROKE
        strokeCap = android.graphics.Paint.Cap.ROUND
    }
    drawPath(path, paint)
}
