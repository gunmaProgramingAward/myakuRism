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

data class MusicTrack(
    val id: Int,
    val title: String,
    val date: String,
    val category: String
)

// TODO 一時的な変数です
val allTracks = listOf(
    MusicTrack(1, "Title by AI1", "2025.06.04", "Happy"),
    MusicTrack(2, "Title by AI2", "2025.06.04", "Sad"),
    MusicTrack(3, "Title by AI3", "2025.06.04", "Angry"),
    MusicTrack(4, "Title by AI4", "2025.06.04", "Happy"),
    MusicTrack(5, "Title by AI5", "2025.06.04", "Surprised"),
    MusicTrack(6, "Title by AI6", "2025.06.04", "Sad"),
    MusicTrack(7, "Title by AI7", "2025.06.05", "Happy"),
    MusicTrack(8, "Title by AI8", "2025.06.05", "Angry"),
)

@Composable
fun MusicLibraryScreen() {
    val categories = listOf("All", "Happy", "Sad", "Angry", "Surprised")
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredTracks = if (selectedCategory == "All") {
        allTracks
    } else {
        allTracks.filter { it.category == selectedCategory }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CategoryChips(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { category ->
                selectedCategory = category
            }
        )
        AlbumGrid(tracks = filteredTracks)
    }
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
            .padding(horizontal = 16.dp, vertical = 8.dp)
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