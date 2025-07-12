package com.example.myaku_rismu.feature.home.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.decode.GifDecoder
import coil.request.ImageRequest
import coil.ImageLoader
import androidx.compose.ui.platform.LocalContext
import com.example.myaku_rismu.R

@Composable
fun GifImageLoader(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(R.drawable.my_gif)
                .build(),
            imageLoader = imageLoader
        ),
        contentDescription = "GIF Animation",
        modifier = modifier
    )
}