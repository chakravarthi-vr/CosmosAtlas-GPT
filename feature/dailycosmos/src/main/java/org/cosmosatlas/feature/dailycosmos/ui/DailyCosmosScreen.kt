package org.cosmosatlas.feature.dailycosmos.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import org.cosmosatlas.core.designsystem.component.GlassmorphicCard
import org.cosmosatlas.core.designsystem.theme.DeepViolet
import org.cosmosatlas.core.designsystem.theme.NeonCyan
import org.cosmosatlas.core.designsystem.theme.SpaceBlack
import org.cosmosatlas.core.designsystem.theme.SpaceDarkSurface
import org.cosmosatlas.core.designsystem.theme.StellarAmber
import org.cosmosatlas.core.designsystem.theme.SupernovaRed
import org.cosmosatlas.core.designsystem.theme.TextMuted
import org.cosmosatlas.core.designsystem.theme.TextPrimary
import org.cosmosatlas.core.designsystem.theme.TextSecondary
import org.cosmosatlas.core.model.ApodRecord
import org.cosmosatlas.feature.dailycosmos.viewmodel.DailyCosmosIntent
import org.cosmosatlas.feature.dailycosmos.viewmodel.DailyCosmosViewModel

@Composable
fun DailyCosmosScreen(
    viewModel: DailyCosmosViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize().background(SpaceBlack)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "DAILY COSMOS",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonCyan
                    )
                    Text(
                        text = "Astronomy Picture of the Day",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                IconButton(onClick = { viewModel.onIntent(DailyCosmosIntent.OnRefresh) }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = NeonCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tab Selector: Today vs Saved
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = !state.showingBookmarksTab,
                    onClick = { viewModel.onIntent(DailyCosmosIntent.OnSwitchTab(false)) },
                    label = { Text("TODAY'S APOD") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonCyan,
                        selectedLabelColor = SpaceBlack,
                        containerColor = SpaceDarkSurface,
                        labelColor = TextSecondary
                    )
                )
                FilterChip(
                    selected = state.showingBookmarksTab,
                    onClick = { viewModel.onIntent(DailyCosmosIntent.OnSwitchTab(true)) },
                    label = { Text("SAVED BOOKMARKS (${state.bookmarkedApods.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonCyan,
                        selectedLabelColor = SpaceBlack,
                        containerColor = SpaceDarkSurface,
                        labelColor = TextSecondary
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (state.isLoading && state.currentApod == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = NeonCyan)
                }
            } else if (state.showingBookmarksTab) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (state.bookmarkedApods.isEmpty()) {
                        item {
                            Text(
                                text = "No saved APOD entries yet. Tap the bookmark icon to save celestial vistas offline.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextMuted,
                                modifier = Modifier.padding(top = 32.dp)
                            )
                        }
                    } else {
                        items(state.bookmarkedApods, key = { it.date }) { item ->
                            ApodCard(
                                apod = item,
                                onToggleBookmark = { viewModel.onIntent(DailyCosmosIntent.OnToggleBookmark(item)) },
                                onInspectHd = { viewModel.onIntent(DailyCosmosIntent.OnToggleZoomModal(true)) }
                            )
                        }
                    }
                }
            } else {
                state.currentApod?.let { apod ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        item {
                            ApodCard(
                                apod = apod,
                                onToggleBookmark = { viewModel.onIntent(DailyCosmosIntent.OnToggleBookmark(apod)) },
                                onInspectHd = { viewModel.onIntent(DailyCosmosIntent.OnToggleZoomModal(true)) }
                            )
                        }
                    }
                }
            }
        }

        // Pinch-to-zoom HD Modal
        if (state.isZoomModalOpen && state.currentApod != null) {
            HdZoomModal(
                imageUrl = state.currentApod?.hdUrl ?: state.currentApod?.url ?: "",
                title = state.currentApod?.title ?: "Celestial Zoom",
                onDismiss = { viewModel.onIntent(DailyCosmosIntent.OnToggleZoomModal(false)) }
            )
        }
    }
}

@Composable
private fun ApodCard(
    apod: ApodRecord,
    onToggleBookmark: () -> Unit,
    onInspectHd: () -> Unit
) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        // Date & Bookmark row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = apod.date,
                style = MaterialTheme.typography.labelSmall,
                color = StellarAmber
            )
            IconButton(onClick = onToggleBookmark) {
                Icon(
                    imageVector = if (apod.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (apod.isBookmarked) StellarAmber else TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Image with Inspect HD icon
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SpaceDarkSurface)
                .clickable(onClick = onInspectHd)
        ) {
            AsyncImage(
                model = apod.url,
                contentDescription = apod.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xCC05070D)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ZoomIn,
                        contentDescription = "Zoom",
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "PINCH HD",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonCyan
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = apod.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        apod.copyright?.let { cr ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Credit: $cr",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = apod.explanation,
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
    }
}

@Composable
private fun HdZoomModal(
    imageUrl: String,
    title: String,
    onDismiss: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Close Button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(20.dp)
            )

            // Zoomable and Pannable AsyncImage
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(1f, 8f)
                            offsetX += pan.x
                            offsetY += pan.y
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offsetX,
                            translationY = offsetY
                        ),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
