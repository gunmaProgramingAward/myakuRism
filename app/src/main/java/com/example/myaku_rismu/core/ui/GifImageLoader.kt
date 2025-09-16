package com.example.myaku_rismu.core.ui

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil3.ImageLoader
import coil3.compose.rememberAsyncImagePainter
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import com.example.myaku_rismu.R

@Composable
fun GifImageLoader(
    modifier: Modifier = Modifier,
    color: Color? = null,
    gitResId: Int
) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(AnimatedImageDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Image(
        painter = rememberAsyncImagePainter(
            model = gitResId,
            imageLoader = imageLoader
        ),
        contentDescription = stringResource(R.string.gif_animation),
        colorFilter = color?.let { ColorFilter.tint(color) },
        modifier = modifier
    )
}