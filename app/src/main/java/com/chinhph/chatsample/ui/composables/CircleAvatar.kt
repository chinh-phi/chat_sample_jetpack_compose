package com.chinhph.chatsample.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.chinhph.chatsample.R

@Composable
fun CircleAvatar(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    imageUrl: String? = null
) {
    Image(
        painter = if (imageUrl.isNullOrEmpty())
            painterResource(R.drawable.avatar_sample)
        else
            rememberAsyncImagePainter(imageUrl),
        contentDescription = "avatar",
        contentScale = ContentScale.Crop,            // crop the image if it's not a square
        modifier = modifier
            .size(size)
            .clip(CircleShape)                       // clip to the circle shape
            .border(2.dp, Color.Gray, CircleShape)   // add a border (optional)
    )
}