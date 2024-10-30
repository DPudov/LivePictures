package com.dpudov.livepictures.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dpudov.livepictures.presentation.ui.controls.DrawingBar
import com.dpudov.livepictures.presentation.ui.controls.LiveCanvas
import com.dpudov.livepictures.presentation.ui.controls.Toolbar
import com.dpudov.livepictures.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentFrame by viewModel.currentFrame.collectAsState()
    val currentInstrument by viewModel.selectedInstrument.collectAsState()
    val currentColor by viewModel.selectedColor.collectAsState()
    var isColorPadVisible by remember { mutableStateOf(false) }
    var isColorPickerVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .fillMaxSize()
    ) {
        LiveCanvas(
            frame = currentFrame,
            instrument = currentInstrument,
            color = Color(currentColor).toArgb(),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(
                    horizontal = 16.dp,
                    vertical = 80.dp
                )
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxSize()
        )
        Toolbar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxWidth(),
            onAddFrame = viewModel::addFrame,
            onDeleteFrame = viewModel::deleteFrame,
            onShowFrames = viewModel::showFrames
        )
        DrawingBar(
            selectedColor = Color(currentColor),
            isColorPadVisible = isColorPadVisible,
            onColorPadToggle = {
                isColorPadVisible = !isColorPadVisible
            },
            onColorSelectionChanged = { color ->
                viewModel.selectColor(color.value)
            },
            onPaletteClick = {
                isColorPickerVisible = !isColorPickerVisible
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxWidth(),
            selectedInstrument = currentInstrument,
            onSelection = viewModel::selectInstrument
        )
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        LiveCanvas(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(
                    horizontal = 16.dp,
                    vertical = 80.dp
                )
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxSize()
        )
        Toolbar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxWidth()
        )
        DrawingBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxWidth()
        )
    }
}