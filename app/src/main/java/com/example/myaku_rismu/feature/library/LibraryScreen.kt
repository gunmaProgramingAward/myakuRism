package com.example.myaku_rismu.feature.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import com.example.myaku_rismu.ui.theme.Typography
import androidx.compose.runtime.Composable
import com.example.myaku_rismu.core.AppState
import androidx.hilt.navigation.compose.hiltViewModel

enum class MusicCategory(val displayName: String) {
    ALL("All"),
    HAPPY("Happy"),
    SAD("Sad"),
    ANGRY("Angry"),
    SURPRISED("Surprised");

    companion object {
        fun fromDisplayName(displayName: String): MusicCategory? {
            return entries.find { it.displayName == displayName }
        }
    }
}
@Composable
fun LibraryScreen(
    appState: AppState,
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    MusicLibraryScreen(
        uiState = uiState,
        onCategorySelected = { category ->
            viewModel.onEvent(LibraryUiEvent.SelectCategory(category))
        },
        modifier = modifier
    )
}

@Composable
fun MusicLibraryScreen(
    uiState: LibraryState,
    onCategorySelected: (MusicCategory) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CategoryChips(
            categories = uiState.categories,
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = onCategorySelected
        )
        AlbumGrid(tracks = uiState.tracks)
    }
}


@Composable
fun CategoryChips(
    categories: List<MusicCategory>,
    selectedCategory: MusicCategory,
    onCategorySelected: (MusicCategory) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = (category == selectedCategory)
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = { Text(category.displayName) },
                shape = RoundedCornerShape(8.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                border = null
            )
        }
    }
}

@Composable
fun AlbumGrid(tracks: List<MusicTrack>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(tracks) { track ->
            AlbumItem(track = track)
        }
    }
}

@Composable
fun AlbumItem(track: MusicTrack) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            onClick = {},
            modifier = Modifier.aspectRatio(1f),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = track.title, style = Typography.bodyLarge)
        Text(text = track.date, style = Typography.bodySmall, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun MusicLibraryScreenPreview() {
    MaterialTheme {
        val previewUiState = LibraryState(
            categories = MusicCategory.entries,
            selectedCategory = MusicCategory.ALL,
            tracks = listOf(
                MusicTrack(1, "Preview Track 1", "2023.01.01", MusicCategory.HAPPY),
                MusicTrack(2, "Preview Track 2", "2023.01.02", MusicCategory.SAD),
                MusicTrack(3, "Preview Track 3", "2023.01.03", MusicCategory.ANGRY),
            )
        )
        MusicLibraryScreen(
            uiState = previewUiState,
            onCategorySelected = { selectedCategory ->
                println("Preview: Category Selected - $selectedCategory") // 選択できているか確認用
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
