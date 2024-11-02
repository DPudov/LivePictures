package com.dpudov.livepictures.presentation.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dpudov.domain.model.Instrument
import com.dpudov.livepictures.R
import kotlin.math.roundToInt

@Composable
@Preview
fun SizePicker(
    initialColor: Color = Color.White,
    initialSize: Float = Instrument.PENCIL_SIZE,
    isPickerVisible: Boolean = false,
    onPickerToggle: () -> Unit = {},
    onSizeSelected: (Float) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (isPickerVisible) {
        SizeSelectionDialog(
            initialColor = initialColor,
            initialSize = initialSize,
            onDismiss = onPickerToggle,
            onNegativeClick = onPickerToggle,
            onPositiveClick = onSizeSelected
        )
    }
}

@Composable
fun SizeSelectionDialog(
    initialColor: Color,
    initialSize: Float,
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: (Float) -> Unit
) {
    var size by remember { mutableFloatStateOf(initialSize) }
    val sizeInDp = size.roundToInt().dp

    Dialog(onDismissRequest = onDismiss) {

        Box(
            Modifier
                .shadow(1.dp, RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.choose_stroke_size),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp, vertical = 20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(sizeInDp)
                            .background(
                                color = initialColor,
                                shape = RoundedCornerShape(8.dp)
                            )
                    )
                }

                SizeSlider(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp)
                        .fillMaxWidth(),
                    title = stringResource(R.string.size),
                    size = size,
                    onSizeChanged = {
                        size = it
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(MaterialTheme.colorScheme.surfaceDim),
                    verticalAlignment = Alignment.CenterVertically

                ) {

                    TextButton(
                        onClick = onNegativeClick,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    TextButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = {
                            onPositiveClick(size)
                        },
                    ) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}

@Composable
fun SizeSlider(
    modifier: Modifier,
    title: String,
    valueRange: ClosedFloatingPointRange<Float> = Instrument.PENCIL_SIZE..Instrument.MAX_SIZE,
    size: Float,
    onSizeChanged: (Float) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Row(modifier, verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(8.dp))
            Slider(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f),
                value = size,
                onValueChange = { onSizeChanged(it) },
                valueRange = valueRange,
                onValueChangeFinished = {}
            )

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = size.toInt().toString(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                modifier = Modifier.width(30.dp)
            )
        }
    }

}
