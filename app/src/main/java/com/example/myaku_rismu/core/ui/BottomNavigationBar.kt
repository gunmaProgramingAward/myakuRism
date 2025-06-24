package com.example.myaku_rismu.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.navigation.CalenderRoute
import com.example.myaku_rismu.core.navigation.HomeRoute
import com.example.myaku_rismu.core.navigation.LibraryRoute
import com.example.myaku_rismu.core.navigation.SettingsRoute
import com.example.myaku_rismu.core.utill.IconResource
import com.example.myaku_rismu.ui.theme.customTheme

enum class NavigationItem(
    val route: Any,
    val selectedIcon: IconResource,
    val unselectedIcon: IconResource,
    val label: String
) {
    HOME(
        route = HomeRoute(),
        selectedIcon = IconResource.Vector(Icons.Filled.Favorite),
        unselectedIcon = IconResource.Vector(Icons.Outlined.FavoriteBorder),
        label = "ホーム"
    ),
    CALENDAR(
        route = CalenderRoute,
        selectedIcon = IconResource.Vector(Icons.Filled.DateRange),
        unselectedIcon = IconResource.Vector(Icons.Outlined.DateRange),
        label = "カレンダー"
    ),
    LIBRARY(
        route = LibraryRoute,
        selectedIcon = IconResource.Drawable(R.drawable.outline_music_note_24),
        unselectedIcon = IconResource.Drawable(R.drawable.outline_music_note_24),
        label = "ライブラリ"
    ),
    SETTING(
        route = SettingsRoute,
        selectedIcon = IconResource.Vector(Icons.Filled.Settings),
        unselectedIcon = IconResource.Vector(Icons.Outlined.Settings),
        label = "プロフィール"
    )
}

@Composable
fun BottomNavigationBar(
    items: List<NavigationItem>,
    onNavigate: (NavigationItem) -> Unit,
    selectedItem: NavigationItem,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        items.forEach { item ->
            val isSelected = item == selectedItem
            val icon = if (isSelected) item.selectedIcon else item.unselectedIcon
            val tint = if (isSelected) MaterialTheme.customTheme.healthDetailHeartRateThemeColor
            else MaterialTheme.colorScheme.onSurfaceVariant

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item) },
                icon = {
                    when (icon) {
                        is IconResource.Vector -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = item.label,
                            tint = tint
                        )
                        is IconResource.Drawable -> Icon(
                            painter = androidx.compose.ui.res.painterResource(id = icon.id),
                            contentDescription = item.label,
                            tint = tint
                    )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelMedium,
                        color = tint
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    var selectedItem by remember { mutableStateOf(NavigationItem.HOME) }

    BottomNavigationBar(
        items = NavigationItem.entries,
        selectedItem = selectedItem,
        onNavigate = { selectedItem = it }
    )
}
