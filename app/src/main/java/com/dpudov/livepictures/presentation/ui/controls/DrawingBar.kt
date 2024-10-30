package com.dpudov.livepictures.presentation.ui.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dpudov.domain.model.Instrument
import com.dpudov.livepictures.R
import com.dpudov.livepictures.presentation.mapper.toSelectedState
import com.dpudov.livepictures.presentation.model.ButtonState
import com.dpudov.livepictures.presentation.model.SelectedState

@Composable
@Preview
fun DrawingBar(
    selectedInstrument: Instrument = Instrument.Pencil,
    onSelection: (Instrument) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {

        PencilButton(
            selectedState = selectedInstrument.toSelectedState(Instrument.Pencil),
            onClick = {
                onSelection(Instrument.Pencil)
            }
        )
        BrushButton(
            selectedState = selectedInstrument.toSelectedState(Instrument.Brush),
            onClick = {
                onSelection(Instrument.Brush)
            }
        )
        EraserButton(
            selectedState = selectedInstrument.toSelectedState(Instrument.Eraser),
            onClick = {
                onSelection(Instrument.Eraser)
            }
        )
        InstrumentsButton()
    }
}

@Composable
@Preview
fun PencilButton(
    selectedState: SelectedState = SelectedState.Idle,
    onClick: () -> Unit = {}
) {
    InstrumentButton(
        drawableId = R.drawable.ic_pencil,
        selectedState = selectedState,
        onClick = onClick,
        contentDescription = stringResource(R.string.draw_with_pencil)
    )
}

@Composable
@Preview
fun BrushButton(
    selectedState: SelectedState = SelectedState.Idle,
    onClick: () -> Unit = {}
) {
    InstrumentButton(
        drawableId = R.drawable.ic_brush,
        selectedState = selectedState,
        onClick = onClick,
        contentDescription = stringResource(R.string.draw_with_brush)
    )
}

@Composable
@Preview
fun EraserButton(
    selectedState: SelectedState = SelectedState.Idle,
    onClick: () -> Unit = {}
) {
    InstrumentButton(
        drawableId = R.drawable.ic_eraser,
        selectedState = selectedState,
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