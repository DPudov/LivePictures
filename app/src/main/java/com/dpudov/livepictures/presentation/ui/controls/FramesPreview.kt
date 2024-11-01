package com.dpudov.livepictures.presentation.ui.controls

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dpudov.livepictures.R

@Composable
@Preview
fun FramePreviewList(
    frames: List<Bitmap> = emptyList(),
    modifier: Modifier = Modifier,
    loadPrev: (Int) -> Unit = {},
    loadNext: (Int) -> Unit = {}
) {
    val visibleFrames = 5
    val listState = rememberLazyListState()
    val firstVisibleItemList by remember { derivedStateOf { listState.firstVisibleItemIndex } }
    val layoutInfo by remember { derivedStateOf { listState.layoutInfo } }
    LaunchedEffect(firstVisibleItemList) {
        if (listState.firstVisibleItemIndex == 0) {
            loadPrev(visibleFrames)
        }
    }

    LaunchedEffect(layoutInfo.visibleItemsInfo.size) {
        if (layoutInfo.visibleItemsInfo.lastOrNull()?.index == frames.size - 1) {
            loadNext(visibleFrames)
        }
    }
    if (frames.isEmpty()) {
        Row(
            modifier = modifier
                .height(200.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(R.string.frames_not_available_yet)
            )
        }
    }
    LazyRow(
        state = listState,
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(frames) { frame ->
            Image(
                bitmap = frame.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .height(200.dp)
                    .padding(4.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
@Preview
fun FramePreview(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.paper_texture),
            contentDescription = stringResource(R.string.frame_preview)
        )
    }
}