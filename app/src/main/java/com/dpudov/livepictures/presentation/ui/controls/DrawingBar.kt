package com.dpudov.livepictures.presentation.ui.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dpudov.livepictures.R
import com.dpudov.livepictures.presentation.model.ButtonState

@Composable
@Preview
fun DrawingBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        PencilButton()
        BrushButton()
        EraserButton()
        InstrumentsButton()
    }
}

@Composable
@Preview
fun PencilButton(
    buttonState: ButtonState = ButtonState.Inactive,
    onClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_pencil,
        inactiveDrawableId = R.drawable.ic_pencil,
        buttonState = buttonState,
        onClick = onClick,
        contentDescription = stringResource(R.string.draw_with_pencil)
    )
}

@Composable
@Preview
fun BrushButton(
    buttonState: ButtonState = ButtonState.Inactive,
    onClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_brush,
        inactiveDrawableId = R.drawable.ic_brush,
        buttonState = buttonState,
        onClick = onClick,
        contentDescription = stringResource(R.string.draw_with_brush)
    )
}

@Composable
@Preview
fun EraserButton(
    buttonState: ButtonState = ButtonState.Inactive,
    onClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_eraser,
        inactiveDrawableId = R.drawable.ic_eraser,
        buttonState = buttonState,
        onClick = onClick,
        contentDescription = stringResource(R.string.eraser)
    )
}

@Composable
@Preview
fun InstrumentsButton(
    buttonState: ButtonState = ButtonState.Inactive,
    onClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_instruments,
        inactiveDrawableId = R.drawable.ic_instruments,
        buttonState = buttonState,
        onClick = onClick,
        contentDescription = stringResource(R.string.instruments)
    )
}