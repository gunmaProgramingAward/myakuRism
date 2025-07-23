package com.example.myaku_rismu.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.FrameMetricsAggregator.MetricType
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myaku_rismu.core.navigation.CalenderRoute
import com.example.myaku_rismu.core.navigation.HealthDetailRoute
import com.example.myaku_rismu.core.navigation.HomeRoute
import com.example.myaku_rismu.core.navigation.LibraryRoute
import com.example.myaku_rismu.core.navigation.ProfileDetailRoute
import com.example.myaku_rismu.core.navigation.SettingsRoute
import com.example.myaku_rismu.core.ui.NavigationItem

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    ): AppState {
    return remember(
        navController,
    ) {
        AppState(
            navController = navController,
        )
    }
}

@Stable
class AppState(
    val navController: NavHostController,
    ) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val selectedNavigationItem: NavigationItem
        @Composable
        get() {
            val currentRoute = currentDestination?.route
            return NavigationItem.entries.find { item ->
                currentRoute == item.route::class.qualifiedName
            } ?: NavigationItem.HOME
        }

    fun navigateToHome(userId: String?) {
        navController.navigate(HomeRoute(userId = userId))
    }

    fun navigateToSetting() {
        navController.navigate(SettingsRoute)
    }

    fun navigateToProfileDetail() {
        navController.navigate(ProfileDetailRoute)
    }

    fun navigateToCalender() {
        navController.navigate(CalenderRoute)
    }

    fun navigateToLibrary() {
        navController.navigate(LibraryRoute)
    }

    fun navigateToHealthDetail(metricType: String) {
        navController.navigate(HealthDetailRoute)
    }

    fun navigatePopUp() {
        navController.popBackStack()
    }

    fun bottomBarNavigateTo(item: NavigationItem) {
        navController.navigate(item.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
         }
    }
}