package com.dpudov.livepictures.presentation.ui.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dpudov.livepictures.R
import com.dpudov.livepictures.presentation.model.ButtonState

@Composable
@Preview
fun Toolbar(
    modifier: Modifier = Modifier,
    undoState: ButtonState = ButtonState.Inactive,
    redoState: ButtonState = ButtonState.Inactive,
    removeState: ButtonState = ButtonState.Inactive,
    addState: ButtonState = ButtonState.Inactive,
    copyState: ButtonState = ButtonState.Inactive,
    pauseState: ButtonState = ButtonState.Inactive,
    startState: ButtonState = ButtonState.Active,
    onUndo: () -> Unit = {},
    onRedo: () -> Unit = {},
    onDeleteFrame: () -> Unit = {},
    onDeleteAll: () -> Unit = {},
    onAddFrame: () -> Unit = {},
    onCopyFrame: () -> Unit = {},
    onShowFrames: () -> Unit = {},
    onStart: () -> Unit = {},
    onPause: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HistoryControls(
            undoState = undoState,
            redoState = redoState,
            onUndo = onUndo,
            onRedo = onRedo
        )

        FrameControls(
            addState = addState,
            removeState = removeState,
            copyState = copyState,
            onDeleteFrame = onDeleteFrame,
            onDeleteAll = onDeleteAll,
            onAddFrame = onAddFrame,
            onCopyFrame = onCopyFrame,
            onShowFrames = onShowFrames
        )

        AnimationControls(
            startState = startState,
            pauseState = pauseState,
            onPause = onPause,
            onStart = onStart
        )
    }
}


@Composable
@Preview
fun HistoryControls(
    undoState: ButtonState = ButtonState.Inactive,
    redoState: ButtonState = ButtonState.Inactive,
    onUndo: () -> Unit = {},
    onRedo: () -> Unit = {}
) {
    Row {
        UndoButton(
            buttonState = undoState,
            onClick = onUndo
        )

        RedoButton(
            buttonState = redoState,
            onClick = onRedo
        )
    }
}

@Composable
@Preview
fun FrameControls(
    removeState: ButtonState = ButtonState.Inactive,
    addState: ButtonState = ButtonState.Inactive,
    copyState: ButtonState = ButtonState.Inactive,
    onDeleteFrame: () -> Unit = {},
    onDeleteAll: () -> Unit = {},
    onAddFrame: () -> Unit = {},
    onCopyFrame: () -> Unit = {},
    onShowFrames: () -> Unit = {}
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RemoveFrameButton(
            buttonState = removeState,
            onClick = onDeleteFrame,
            onLongClick = onDeleteAll
        )

        AddFrameButton(
            buttonState = addState,
            onClick = onAddFrame
        )

        CopyFrameButton(
            buttonState = copyState,
            onClick = onCopyFrame
        )

        ShowFramesButton(
            buttonState = ButtonState.Active,
            onClick = onShowFrames
        )
    }
}

@Composable
@Preview
fun AnimationControls(
    pauseState: ButtonState = ButtonState.Inactive,
    startState: ButtonState = ButtonState.Active,
    onPause: () -> Unit = {},
    onStart: () -> Unit = {}
) {
    Row {
        PauseButton(
            buttonState = pauseState,
            onClick = onPause
        )

        StartButton(
            buttonState = startState,
            onClick = onStart
        )
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
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_bin,
        inactiveDrawableId = R.drawable.ic_bin,
        buttonState = buttonState,
        onClick = onClick,
        onLongClick = onLongClick,
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
fun CopyFrameButton(
    buttonState: ButtonState = ButtonState.Inactive,
    onClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_frame_copy,
        inactiveDrawableId = R.drawable.ic_frame_copy,
        buttonState = buttonState,
        onClick = onClick,
        contentDescription = stringResource(R.string.copy_frame)
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