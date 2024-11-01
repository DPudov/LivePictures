package com.dpudov.livepictures.presentation.ui.controls

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.dpudov.livepictures.presentation.model.ButtonState

@Composable
fun ActionButton(
    @DrawableRes activeDrawableId: Int,
    @DrawableRes inactiveDrawableId: Int,
    buttonState: ButtonState,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val enabled = buttonState == ButtonState.Active
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
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(
            tint = tint,
            painter = painterResource(id = id),
            contentDescription = contentDescription
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActionButton(
    @DrawableRes activeDrawableId: Int,
    @DrawableRes inactiveDrawableId: Int,
    buttonState: ButtonState,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val enabled = buttonState == ButtonState.Active
    val id = when (buttonState) {
        ButtonState.Active -> activeDrawableId
        ButtonState.Inactive -> inactiveDrawableId
    }
    val tint = when (buttonState) {
        ButtonState.Active -> MaterialTheme.colorScheme.primary
        ButtonState.Inactive -> MaterialTheme.colorScheme.secondary
    }

    Icon(
        modifier = if (enabled) modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ) else modifier,
        tint = tint,
        painter = painterResource(id = id),
        contentDescription = contentDescription
    )
}