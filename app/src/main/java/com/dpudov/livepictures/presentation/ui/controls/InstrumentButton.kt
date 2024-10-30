package com.dpudov.livepictures.presentation.ui.controls

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.dpudov.livepictures.presentation.model.SelectedState

@Composable
fun InstrumentButton(
    selectedState: SelectedState,
    @DrawableRes drawableId: Int,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val backgroundTint = when (selectedState) {
        SelectedState.Idle -> MaterialTheme.colorScheme.primaryContainer
        SelectedState.Selected -> MaterialTheme.colorScheme.tertiaryContainer
    }
    val iconTint = when (selectedState) {
        SelectedState.Idle -> MaterialTheme.colorScheme.onPrimaryContainer
        SelectedState.Selected -> MaterialTheme.colorScheme.onTertiaryContainer
    }
    IconButton(
        modifier = modifier.background(color = backgroundTint),
        onClick = onClick
    ) {
        Icon(
            tint = iconTint,
            painter = painterResource(id = drawableId),
            contentDescription = contentDescription
        )
    }
}