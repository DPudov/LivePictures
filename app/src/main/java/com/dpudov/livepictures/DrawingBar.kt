package com.dpudov.livepictures

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun DrawingBar(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        PencilButton()
        BrushButton()
        EraserButton()
        InstrumentsButton()
    }
}

@Composable
@Preview
fun PencilButton() {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_pencil),
            contentDescription = stringResource(R.string.draw_with_pencil)
        )
    }
}

@Composable
@Preview
fun BrushButton() {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_brush),
            contentDescription = stringResource(R.string.draw_with_brush)
        )
    }
}

@Composable
@Preview
fun EraserButton() {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_eraser),
            contentDescription = stringResource(R.string.eraser)
        )
    }
}

@Composable
@Preview
fun InstrumentsButton() {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_instruments),
            contentDescription = stringResource(R.string.instruments)
        )
    }
}