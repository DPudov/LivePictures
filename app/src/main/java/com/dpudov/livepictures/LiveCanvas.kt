package com.dpudov.livepictures

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@Composable
@Preview
fun LiveCanvas(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        AndroidView(factory = { context ->
            LiveCanvasView(context)
        })
    }
}