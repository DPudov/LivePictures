package com.dpudov.livepictures.presentation.ui.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.dpudov.domain.model.Frame
import com.dpudov.livepictures.R
import com.dpudov.livepictures.presentation.ui.view.LiveCanvasView

@Composable
@Preview
fun LiveCanvas(
    frame: Frame? = null,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (frame != null) {
            Column {
                Text(
                    text = "Frame: $frame"
                )
                AndroidView(factory = { context ->
                    LiveCanvasView(context)
                })
            }

        } else {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.no_frame_exist_add_one_to_start_drawing)
            )
        }
    }
}