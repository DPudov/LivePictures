package com.dpudov.livepictures

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun Toolbar(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
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
    buttonState: ButtonState = ButtonState.Inactive
) {
    val id = when (buttonState) {
        ButtonState.Active -> R.drawable.ic_undo_active
        ButtonState.Inactive -> R.drawable.ic_undo_unactive
    }
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            painter = painterResource(id = id),
            contentDescription = stringResource(R.string.undo_recent_action)
        )
    }
}

@Composable
@Preview
fun RedoButton(
    buttonState: ButtonState = ButtonState.Inactive
) {
    val id = when (buttonState) {
        ButtonState.Active -> R.drawable.ic_redo_active
        ButtonState.Inactive -> R.drawable.ic_redo_unactive
    }
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            painter = painterResource(id = id),
            contentDescription = stringResource(R.string.redo_recent_action)
        )
    }
}

@Composable
@Preview
fun RemoveFrameButton() {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_bin),
            contentDescription = stringResource(R.string.remove_frame)
        )
    }
}

@Composable
@Preview
fun AddFrameButton() {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add_frame),
            contentDescription = stringResource(R.string.add_frame)
        )
    }
}

@Composable
@Preview
fun ShowFramesButton() {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_layers),
            contentDescription = stringResource(R.string.show_frames)
        )
    }
}


@Composable
@Preview
fun PauseButton(
    buttonState: ButtonState = ButtonState.Inactive
) {
    val id = when (buttonState) {
        ButtonState.Active -> R.drawable.ic_pause_active
        ButtonState.Inactive -> R.drawable.ic_pause_unactive
    }
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            painter = painterResource(id = id), contentDescription =
            stringResource(R.string.pause_animation)
        )
    }
}

@Composable
@Preview
fun StartButton(
    buttonState: ButtonState = ButtonState.Inactive
) {
    val id = when (buttonState) {
        ButtonState.Active -> R.drawable.ic_start_active
        ButtonState.Inactive -> R.drawable.ic_start_unactive
    }
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            painter = painterResource(id = id),
            contentDescription = stringResource(R.string.start_animation)
        )
    }
}