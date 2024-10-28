package com.dpudov.livepictures

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun MainScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        LiveCanvas(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
        )
        Toolbar(modifier = Modifier.align(Alignment.TopCenter))
        DrawingBar(modifier = Modifier.align(Alignment.BottomCenter))
    }
}