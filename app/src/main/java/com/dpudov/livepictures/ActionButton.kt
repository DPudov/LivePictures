package com.dpudov.livepictures

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    @DrawableRes activeDrawableId: Int,
    @DrawableRes inactiveDrawableId: Int,
    buttonState: ButtonState,
    onClick: () -> Unit,
    contentDescription: String
) {
    val id = when (buttonState) {
        ButtonState.Active -> activeDrawableId
        ButtonState.Inactive -> inactiveDrawableId
    }
    val tint = when (buttonState) {
        ButtonState.Active -> MaterialTheme.colorScheme.primary
        ButtonState.Inactive -> MaterialTheme.colorScheme.secondary
    }
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            tint = tint,
            painter = painterResource(id = id),
            contentDescription = contentDescription
        )
    }
}