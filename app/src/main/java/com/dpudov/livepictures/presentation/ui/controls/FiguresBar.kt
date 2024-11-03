package com.dpudov.livepictures.presentation.ui.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dpudov.domain.model.Instrument
import com.dpudov.livepictures.R
import com.dpudov.livepictures.presentation.mapper.toSelectedState
import com.dpudov.livepictures.presentation.model.SelectedState

@Composable
@Preview
fun FiguresBar(
    isVisible: Boolean = true,
    currentInstrument: Instrument = Instrument.Pencil,
    modifier: Modifier = Modifier,
    onFigureSelected: (Instrument.Figure) -> Unit = {}
) {
    if (isVisible) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            RectangleButton(
                selectedState = currentInstrument.toSelectedState(Instrument.Figure.Rectangle),
                onClick = {
                    onFigureSelected(Instrument.Figure.Rectangle)
                })
            TriangleButton(
                selectedState = currentInstrument.toSelectedState(Instrument.Figure.Triangle),
                onClick = {
                    onFigureSelected(Instrument.Figure.Triangle)
                })
            CircleButton(
                selectedState = currentInstrument.toSelectedState(Instrument.Figure.Circle),
                onClick = {
                    onFigureSelected(Instrument.Figure.Circle)
                })
        }
    }
}

@Composable
@Preview
fun RectangleButton(
    selectedState: SelectedState = SelectedState.Idle,
    onClick: () -> Unit = {}
) {
    InstrumentButton(
        selectedState = selectedState,
        drawableId = R.drawable.ic_rect,
        onClick = onClick,
        contentDescription = stringResource(R.string.add_rectangle)
    )
}

@Composable
@Preview
fun TriangleButton(
    selectedState: SelectedState = SelectedState.Idle,
    onClick: () -> Unit = {}
) {
    InstrumentButton(
        drawableId = R.drawable.ic_triangle,
        selectedState = selectedState,
        onClick = onClick,
        contentDescription = stringResource(R.string.add_triangle)
    )
}

@Composable
@Preview
fun CircleButton(
    selectedState: SelectedState = SelectedState.Idle,
    onClick: () -> Unit = {}
) {
    InstrumentButton(
        drawableId = R.drawable.ic_circle,
        selectedState = selectedState,
        onClick = onClick,
        contentDescription = stringResource(R.string.add_circle)
    )
}