package com.dpudov.livepictures.presentation.ui.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dpudov.domain.model.Instrument
import com.dpudov.livepictures.R
import com.dpudov.livepictures.presentation.mapper.toSelectedState
import com.dpudov.livepictures.presentation.model.ButtonState
import com.dpudov.livepictures.presentation.model.SelectedState

@Composable
@Preview
fun DrawingBar(
    isColorPadVisible: Boolean = false,
    isPickerVisible: Boolean = false,
    selectedColor: Color = Color.White,
    selectedInstrument: Instrument = Instrument.Pencil,
    onSelection: (Instrument) -> Unit = {},
    onColorPadToggle: () -> Unit = {},
    onColorPickerToggle: () -> Unit = {},
    onColorSelectionChanged: (Color) -> Unit = {},
    onPaletteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ColorPicker(
            isPickerVisible = isPickerVisible,
            onColorSelected = { color ->
                onColorSelectionChanged(color)
                onColorPickerToggle()
            },
            onColorPickerToggle = onColorPickerToggle
        )
        ColorPad(
            isVisible = isColorPadVisible,
            onPaletteClick = onPaletteClick,
            onColorSelected = onColorSelectionChanged
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
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
            ColorButton(
                containerColor = selectedColor,
                onClick = onColorPadToggle
            )
        }
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

@Composable
@Preview
fun ColorPad(
    isVisible: Boolean = false,
    onPaletteClick: () -> Unit = {},
    onColorSelected: (Color) -> Unit = {},
    colorPrimary: Color = MaterialTheme.colorScheme.primary,
    colorSecondary: Color = MaterialTheme.colorScheme.secondary,
    colorTertiary: Color = MaterialTheme.colorScheme.tertiary,
    customColor: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    if (isVisible) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ActionButton(
                activeDrawableId = R.drawable.ic_color_palette,
                inactiveDrawableId = R.drawable.ic_color_palette,
                buttonState = ButtonState.Active,
                onClick = onPaletteClick,
                contentDescription = stringResource(R.string.choose_color)
            )
            ColorButton(
                containerColor = colorPrimary,
                onClick = {
                    onColorSelected(colorPrimary)
                })
            ColorButton(containerColor = colorSecondary,
                onClick = {
                    onColorSelected(colorSecondary)
                })
            ColorButton(containerColor = colorTertiary,
                onClick = {
                    onColorSelected(colorTertiary)
                })
            ColorButton(containerColor = customColor,
                onClick = {
                    onColorSelected(customColor)
                })
        }
    }
}

@Composable
@Preview
fun ColorButton(
    containerColor: Color = Color.Red,
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        modifier = Modifier
            .padding(8.dp)
            .size(32.dp),
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor),
        shape = CircleShape,
        content = {}
    )
}