package com.example.myaku_rismu.core.ui

import androidx.annotation.StringRes
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
import androidx.compose.ui.res.stringResource
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
    @StringRes val label: Int
) {
    HOME(
        route = HomeRoute,
        selectedIcon = IconResource.Vector(Icons.Filled.Favorite),
        unselectedIcon = IconResource.Vector(Icons.Outlined.FavoriteBorder),
        label = R.string.bottom_navigation_bar_home
    ),
    CALENDAR(
        route = CalenderRoute,
        selectedIcon = IconResource.Vector(Icons.Filled.DateRange),
        unselectedIcon = IconResource.Vector(Icons.Outlined.DateRange),
        label = R.string.bottom_navigation_bar_calendar
    ),
    LIBRARY(
        route = LibraryRoute,
        selectedIcon = IconResource.Drawable(R.drawable.outline_music_note_24),
        unselectedIcon = IconResource.Drawable(R.drawable.outline_music_note_24),
        label = R.string.bottom_navigation_bar_library
    ),
    SETTING(
        route = SettingsRoute,
        selectedIcon = IconResource.Vector(Icons.Filled.Settings),
        unselectedIcon = IconResource.Vector(Icons.Outlined.Settings),
        label = R.string.bottom_navigation_bar_profile
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
        containerColor = MaterialTheme.customTheme.bottomNavigationBarBackgroundColor,
        contentColor = MaterialTheme.customTheme.bottomNavigationBarBackgroundColor,
        modifier = modifier
    ) {
        items.forEach { item ->
            val isSelected = item == selectedItem
            val icon = if (isSelected) item.selectedIcon else item.unselectedIcon

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item) },
                icon = {
                    when (icon) {
                        is IconResource.Vector -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = stringResource(id = item.label),
                        )
                        is IconResource.Drawable -> Icon(
                            painter = androidx.compose.ui.res.painterResource(id = icon.id),
                            contentDescription = stringResource(id = item.label),
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(id = item.label),
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.customTheme.bottomNavigationBarSelectedColor,
                    unselectedIconColor = MaterialTheme.customTheme.bottomNavigationBarUnSelectedColor,
                    selectedTextColor = MaterialTheme.customTheme.bottomNavigationBarSelectedColor,
                    unselectedTextColor = MaterialTheme.customTheme.bottomNavigationBarUnSelectedColor,
                    indicatorColor = MaterialTheme.customTheme.bottomNavigationBarBackgroundColor
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
