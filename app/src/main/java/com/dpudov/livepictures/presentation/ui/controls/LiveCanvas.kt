package com.dpudov.livepictures.presentation.ui.controls

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dpudov.domain.model.Frame
import com.dpudov.domain.model.Instrument
import com.dpudov.domain.model.Stroke
import com.dpudov.livepictures.R
import com.dpudov.livepictures.presentation.model.OnStrokeDrawn
import com.dpudov.livepictures.presentation.model.OnToolChanged
import android.graphics.Color as StandardColor

@Composable
@Preview
fun LiveCanvas(
    frame: Frame? = null,
    strokes: List<Stroke> = emptyList(),
    instrument: Instrument = Instrument.Pencil,
    onStrokeDrawn: OnStrokeDrawn = OnStrokeDrawn { _, _, _, _ -> },
    onToolChanged: OnToolChanged = OnToolChanged { },
    color: Int = StandardColor.WHITE,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (frame != null) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.paper_texture),
                contentDescription = stringResource(R.string.canvas_background)
            )
            Canvas(modifier = Modifier.fillMaxSize()) {
                strokes.forEach {
                    drawStroke(it)
                }
            }
//            AndroidView(
//                factory = { context ->
//                    LiveCanvasView(context).apply {
//                        setInstrument(instrument)
//                        setColor(color)
//                        onStrokeDrawnListener = onStrokeDrawn
//                        onToolChangedListener = onToolChanged
//                    }
//                }, update = { view ->
//                    view.setInstrument(instrument)
//                    view.setColor(color)
//                })
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
        )
    )
}