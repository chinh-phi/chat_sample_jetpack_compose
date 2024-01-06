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
import androidx.compose.ui.tooling.preview.Preview
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
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(2.dp, Color.Gray, CircleShape)
    )
}

@Preview
@Composable
fun CircleAvatarPreview() {
    CircleAvatar()
}