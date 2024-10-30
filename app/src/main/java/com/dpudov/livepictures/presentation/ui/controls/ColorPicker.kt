package com.dpudov.livepictures.presentation.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun ColorPicker(
    isPickerVisible: Boolean = false,
    onColorPickerToggle: () -> Unit = {},
    onColorSelected: (Color) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var hue by remember { mutableFloatStateOf(0f) }
    var saturation by remember { mutableFloatStateOf(1f) }
    var brightness by remember { mutableFloatStateOf(1f) }

    val selectedColor = Color.hsv(hue, saturation, brightness)
    onColorSelected(selectedColor)

    if (isPickerVisible) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(16.dp)
        ) {
//            ActionButton(onClick = onColorPickerToggle) {
//
//            }
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, _ ->
                            val x = change.position.x.coerceIn(0f, size.width.toFloat())
                            val y = change.position.y.coerceIn(0f, size.height.toFloat())
                            hue = (x / size.width) * 360f
                            saturation = 1 - (y / size.height)
                        }
                    }
                    .background(brush = hueSaturationBrush())
            )

            Spacer(modifier = Modifier.height(16.dp))

            Slider(
                value = brightness,
                onValueChange = { brightness = it },
                colors = SliderDefaults.colors(
                    thumbColor = selectedColor,
                    activeTrackColor = selectedColor,
                    inactiveTrackColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(selectedColor)
                    .border(2.dp, Color.Black, CircleShape)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
fun hueSaturationBrush(): Brush {
    return Brush.horizontalGradient(
        colors = (0..360 step 60).map { Color.hsv(it.toFloat(), 1f, 1f) }
    )
}
