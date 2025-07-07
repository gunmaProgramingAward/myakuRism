package com.example.myaku_rismu.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.myaku_rismu.core.navigation.HomeRoute

class HomeViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val args: HomeRoute = savedStateHandle.toRoute()
    val userId: String? = args.userId
}