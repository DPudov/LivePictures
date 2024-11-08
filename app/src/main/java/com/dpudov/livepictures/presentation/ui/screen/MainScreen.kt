package com.dpudov.livepictures.presentation.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dpudov.livepictures.R
import com.dpudov.livepictures.presentation.model.GenerationState
import com.dpudov.livepictures.presentation.model.GifPreparationState
import com.dpudov.livepictures.presentation.ui.controls.AdditionalBar
import com.dpudov.livepictures.presentation.ui.controls.DrawingBar
import com.dpudov.livepictures.presentation.ui.controls.FramePreviewList
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
    val previousItems by viewModel.previousItems.collectAsState()
    val currentItems by viewModel.currentItems.collectAsState()
    val undoState by viewModel.undoState.collectAsState()
    val redoState by viewModel.redoState.collectAsState()
    val addState by viewModel.addState.collectAsState()
    val removeState by viewModel.removeState.collectAsState()
    val copyState by viewModel.copyState.collectAsState()
    val startState by viewModel.startState.collectAsState()
    val pauseState by viewModel.pauseState.collectAsState()
    val animationState by viewModel.animationState.collectAsState()
    val framePreviews by viewModel.framePreviews.collectAsState()
    val animation by viewModel.currentAnimation.collectAsState()
    val gifPreparationState by viewModel.gifPreparationState.collectAsState()
    val strokeSize by viewModel.selectedSize.collectAsState()
    val generationState by viewModel.generationState.collectAsState()

    var isColorPadVisible by remember { mutableStateOf(false) }
    var isColorPickerVisible by remember { mutableStateOf(false) }
    var isSizePickerVisible by remember { mutableStateOf(false) }
    var isFramePreviewVisible by remember { mutableStateOf(false) }
    var isFigurePadVisible by remember { mutableStateOf(false) }
    var generationCount by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Toolbar(
                undoState = undoState,
                redoState = redoState,
                startState = startState,
                pauseState = pauseState,
                removeState = removeState,
                addState = addState,
                copyState = copyState,
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .fillMaxWidth(),
                onAddFrame = {
                    isFramePreviewVisible = false
                    viewModel.addFrame()
                },
                onDeleteFrame = {
                    isFramePreviewVisible = false
                    Toast.makeText(
                        context,
                        context.getString(R.string.hold_longer_to_delete_all_frames),
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.deleteFrame()
                },
                onDeleteAll = {
                    isFramePreviewVisible = false
                    viewModel.deleteAll()
                },
                onCopyFrame = {
                    isFramePreviewVisible = false
                    viewModel.copyFrame()
                },
                onShowFrames = {
                    isFramePreviewVisible = !isFramePreviewVisible
                    viewModel.updatePreviewCache()
                },
                onUndo = {
                    isFramePreviewVisible = false
                    viewModel.undo()
                },
                onRedo = {
                    isFramePreviewVisible = false
                    viewModel.redo()
                },
                onStart = {
                    isFramePreviewVisible = false
                    viewModel.startAnimation()
                },
                onPause = {
                    isFramePreviewVisible = false
                    viewModel.pauseAnimation()
                }
            )

            AdditionalBar(
                modifier = Modifier
                    .padding(
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .fillMaxWidth(),
                onFpsSelected = viewModel::selectFps,
                defaultFps = animation?.fps ?: 1,
                onShare = {
                    isFramePreviewVisible = false
                    viewModel.shareAnimation(context)
                },
                currentValue = generationCount,
                onValueChange = { newValue ->
                    generationCount = newValue
                },
                onGenerate = {
                    isFramePreviewVisible = false
                    viewModel.generateFrames(it)
                }
            )

            if (gifPreparationState == GifPreparationState.Idle && generationState == GenerationState.Idle) {
                LiveCanvas(
                    animationState = animationState,
                    frame = currentFrame,
                    instrument = currentInstrument,
                    size = strokeSize,
                    color = Color(currentColor).toArgb(),
                    previousItems = previousItems,
                    items = currentItems,
                    onItemDrawn = viewModel.onItemDrawn,
                    onToolChanged = viewModel.onToolChanged,
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .weight(1f)
                        .fillMaxWidth()
                )
            } else if (gifPreparationState != GifPreparationState.Idle) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            } else if (generationState is GenerationState.Generating) {
                val state = generationState as? GenerationState.Generating
                if (state != null) {
                    val progress = state.number.toFloat() / state.total.toFloat()
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(1f)
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        progress = { progress }
                    )
                }
            }

            DrawingBar(
                currentSize = strokeSize,
                selectedColor = Color(currentColor),
                isColorPadVisible = isColorPadVisible,
                isPickerVisible = isColorPickerVisible,
                isSizePickerVisible = isSizePickerVisible,
                isFiguresPadVisible = isFigurePadVisible,
                onColorPickerToggle = {
                    isColorPickerVisible = !isColorPickerVisible
                },
                onSizePickerToggle = {
                    isSizePickerVisible = !isSizePickerVisible
                },
                onColorPadToggle = {
                    isColorPadVisible = !isColorPadVisible
                },
                onColorSelectionChanged = { color ->
                    viewModel.selectColor(color.value)
                    isColorPadVisible = false
                },
                onSizeSelectionChanged = viewModel::selectSize,
                onPaletteClick = {
                    isColorPickerVisible = !isColorPickerVisible
                },
                onInstrumentsClick = {
                    isFigurePadVisible = !isFigurePadVisible
                },
                modifier = Modifier
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
            if (isFramePreviewVisible) {
                FramePreviewList(
                    modifier = Modifier
                        .clickable {
                            isFramePreviewVisible = false
                        }
                        .padding(16.dp),
                    frames = framePreviews,
                    loadNext = { count ->
                        viewModel.loadNextFrames()
                    },
                    loadPrev = { count ->
                        viewModel.loadPreviousFrames()
                    },
                    onItemClick = viewModel::selectFrame
                )
            }
        }


    }
}

@Composable
@Preview
fun MainScreenPreview() {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Toolbar(
                modifier = Modifier
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
            AdditionalBar(
                modifier = Modifier
                    .padding(
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .fillMaxWidth()
            )

            LiveCanvas(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .weight(1f)
                    .fillMaxWidth()
            )

            DrawingBar(
                modifier = Modifier
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
}