package com.dpudov.livepictures.presentation.ui.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dpudov.domain.model.Animation
import com.dpudov.livepictures.R
import com.dpudov.livepictures.presentation.model.ButtonState

@Composable
@Preview
fun AdditionalBar(
    modifier: Modifier = Modifier,
    defaultFps: Int = 1,
    onShare: () -> Unit = {},
    onFpsSelected: (Int) -> Unit = {}
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