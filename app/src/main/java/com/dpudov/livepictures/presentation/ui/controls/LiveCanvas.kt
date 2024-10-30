package com.dpudov.livepictures.presentation.ui.controls

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.dpudov.domain.model.Frame
import com.dpudov.domain.model.Instrument
import com.dpudov.livepictures.R
import com.dpudov.livepictures.presentation.ui.view.LiveCanvasView

@Composable
@Preview
fun LiveCanvas(
    frame: Frame? = null,
    instrument: Instrument = Instrument.Pencil,
    color: Int = Color.WHITE,
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
            AndroidView(
                factory = { context ->
                    LiveCanvasView(context)
                }, update = { view ->
                    view.setInstrument(instrument)
                    view.setColor(color)
                })
        } else {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.no_frame_exist_add_one_to_start_drawing)
            )
        }
    }
}