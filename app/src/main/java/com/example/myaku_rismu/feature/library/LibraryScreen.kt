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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myaku_rismu.core.AppState

data class MusicTrack(
    val id: Int,
    val title: String,
    val date: String,
    val category: String
)

@Composable
fun LibraryScreen(appState: AppState, modifier: Modifier = Modifier) {
}

@Composable
fun MusicLibraryScreen(
    viewModel: LibraryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CategoryChips(
            categories = uiState.categories,
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = { category ->
                viewModel.onEvent(LibraryUiEvent.SelectCategory(category))
            }
        )
        AlbumGrid(tracks = uiState.tracks)    }
}


@Composable
fun CategoryChips(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
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
            FilterChip(
                selected = (category == selectedCategory),
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                shape = RoundedCornerShape(16.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
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
        MusicLibraryScreen()
    }
}
