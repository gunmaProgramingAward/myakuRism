package com.example.myaku_rismu.core.utill

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

sealed class IconResource {
    data class Vector(val imageVector: ImageVector) : IconResource()
    data class Drawable(@DrawableRes val id: Int) : IconResource()
}