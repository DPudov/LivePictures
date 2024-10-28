package com.dpudov.livepictures

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun Toolbar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HistoryControls()

        FrameControls()

        AnimationControls()
    }
}


@Composable
@Preview
fun HistoryControls() {
    Row {
        UndoButton()

        RedoButton()
    }
}

@Composable
@Preview
fun FrameControls() {
    Row {
        RemoveFrameButton()

        AddFrameButton()

        ShowFramesButton()
    }
}

@Composable
@Preview
fun AnimationControls() {
    Row {
        PauseButton()

        StartButton()
    }
}

@Composable
@Preview
fun UndoButton(
    buttonState: ButtonState = ButtonState.Inactive,
    onClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_undo_active,
        inactiveDrawableId = R.drawable.ic_undo_unactive,
        buttonState = buttonState,
        onClick = onClick,
        contentDescription = stringResource(R.string.undo_recent_action)
    )
}

@Composable
@Preview
fun RedoButton(
    buttonState: ButtonState = ButtonState.Inactive,
    onClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_redo_active,
        inactiveDrawableId = R.drawable.ic_redo_unactive,
        buttonState = buttonState,
        onClick = onClick,
        contentDescription = stringResource(R.string.redo_recent_action)
    )
}

@Composable
@Preview
fun RemoveFrameButton(
    buttonState: ButtonState = ButtonState.Inactive,
    onClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_bin,
        inactiveDrawableId = R.drawable.ic_bin,
        buttonState = buttonState,
        onClick = onClick,
        contentDescription = stringResource(R.string.remove_frame)
    )
}

@Composable
@Preview
fun AddFrameButton(
    buttonState: ButtonState = ButtonState.Inactive,
    onClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_add_frame,
        inactiveDrawableId = R.drawable.ic_add_frame,
        buttonState = buttonState,
        onClick = onClick,
        contentDescription = stringResource(R.string.add_frame)
    )
}

@Composable
@Preview
fun ShowFramesButton(
    buttonState: ButtonState = ButtonState.Inactive,
    onClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_layers,
        inactiveDrawableId = R.drawable.ic_layers,
        buttonState = buttonState,
        onClick = onClick,
        contentDescription = stringResource(R.string.instruments)
    )
}


@Composable
@Preview
fun PauseButton(
    buttonState: ButtonState = ButtonState.Inactive,
    onClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_pause_active,
        inactiveDrawableId = R.drawable.ic_pause_unactive,
        buttonState = buttonState,
        onClick = onClick,
        contentDescription = stringResource(R.string.pause_animation)
    )
}

@Composable
@Preview
fun StartButton(
    buttonState: ButtonState = ButtonState.Inactive,
    onClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_start_active,
        inactiveDrawableId = R.drawable.ic_start_unactive,
        buttonState = buttonState,
        onClick = onClick,
        contentDescription = stringResource(R.string.start_animation)
    )
}