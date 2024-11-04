package com.dpudov.livepictures.presentation.ui.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dpudov.domain.model.Animation
import com.dpudov.livepictures.R
import com.dpudov.livepictures.presentation.model.ButtonState

@Composable
@Preview
fun AdditionalBar(
    modifier: Modifier = Modifier,
    defaultFps: Int = 1,
    onShare: () -> Unit = {},
    onFpsSelected: (Int) -> Unit = {},
    currentValue: Int = 0,
    onValueChange: (Int) -> Unit = {},
    onGenerate: (Int) -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FpsSelectorButton(
            selectedFps = defaultFps,
            onFpsSelected = onFpsSelected
        )
        GenerationControls(
            modifier = Modifier.weight(1f),
            currentValue = currentValue,
            onValueChange = onValueChange,
            onGenerate = onGenerate
        )
        ShareButton(
            buttonState = ButtonState.Active,
            onClick = onShare
        )
    }
}

@Composable
@Preview
fun ShareButton(
    buttonState: ButtonState = ButtonState.Inactive,
    onClick: () -> Unit = {}
) {
    ActionButton(
        activeDrawableId = R.drawable.ic_share,
        inactiveDrawableId = R.drawable.ic_share,
        buttonState = buttonState,
        onClick = onClick,
        contentDescription = stringResource(R.string.share_animation_as_gif)
    )
}

@Composable
@Preview
fun FpsSelectorButton(
    availableFps: List<Int> = listOf(1, 2, 4, 8, 16, 24),
    selectedFps: Int = Animation.DEFAULT_FPS,
    onFpsSelected: (Int) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Row {
        Button(onClick = { expanded = true }) {
            Text(text = stringResource(R.string.fps, selectedFps))
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            availableFps.forEach { fps ->
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(R.string.fps, fps))
                    },
                    onClick = {
                        expanded = false
                        onFpsSelected(fps)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun PositiveIntInputField(
    value: Int = 0,
    onValueChange: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(value.toString()) }

    TextField(
        value = text,
        onValueChange = { newText ->
            // Check if the new text is a valid positive integer or empty
            if (newText.isEmpty() || newText.toIntOrNull()?.let { it > 0 } == true) {
                text = newText
                newText.toIntOrNull()?.let { onValueChange(it) }
            }
        },
        label = {
            Text(
                text = stringResource(R.string.generate_n_frames)
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}

@Composable
@Preview
fun GenerationControls(
    modifier: Modifier = Modifier,
    currentValue: Int = 0,
    onValueChange: (Int) -> Unit = {},
    onGenerate: (Int) -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PositiveIntInputField(
            value = currentValue,
            onValueChange = onValueChange,
            modifier = Modifier.padding(8.dp)
        )
        Button(
            modifier = Modifier.padding(8.dp),
            onClick = {
                onGenerate(currentValue)
            }
        ) {
            Text(text = stringResource(R.string.generate))
        }
    }
}
