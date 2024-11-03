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
fun FiguresBar(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        RectangleButton()
        TriangleButton()
        CircleButton()
    }
}

@Composable
@Preview
fun RectangleButton(
    buttonState: ButtonState = ButtonState.Inactive,
    onClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_rect,
        inactiveDrawableId = R.drawable.ic_rect,
        buttonState = buttonState,
        onClick = onClick,
        contentDescription = stringResource(R.string.add_rectangle)
    )
}

@Composable
@Preview
fun TriangleButton(
    buttonState: ButtonState = ButtonState.Inactive,
    onClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_triangle,
        inactiveDrawableId = R.drawable.ic_triangle,
        buttonState = buttonState,
        onClick = onClick,
        contentDescription = stringResource(R.string.add_triangle)
    )
}

@Composable
@Preview
fun CircleButton(
    buttonState: ButtonState = ButtonState.Inactive,
    onClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_circle,
        inactiveDrawableId = R.drawable.ic_circle,
        buttonState = buttonState,
        onClick = onClick,
        contentDescription = stringResource(R.string.add_circle)
    )
}