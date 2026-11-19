package org.cosmosatlas.feature.solar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import org.cosmosatlas.core.designsystem.component.GlassmorphicCard
import org.cosmosatlas.core.designsystem.component.TelemetryChip
import org.cosmosatlas.core.designsystem.theme.AuroraGreen
import org.cosmosatlas.core.designsystem.theme.ElectricBlue
import org.cosmosatlas.core.designsystem.theme.NeonCyan
import org.cosmosatlas.core.designsystem.theme.SpaceBlack
import org.cosmosatlas.core.designsystem.theme.SpaceBorder
import org.cosmosatlas.core.designsystem.theme.SpaceCardSurface
import org.cosmosatlas.core.designsystem.theme.SpaceDarkSurface
import org.cosmosatlas.core.designsystem.theme.StellarAmber
import org.cosmosatlas.core.designsystem.theme.SupernovaRed
import org.cosmosatlas.core.designsystem.theme.TextMuted
import org.cosmosatlas.core.designsystem.theme.TextPrimary
import org.cosmosatlas.core.designsystem.theme.TextSecondary
import org.cosmosatlas.core.model.NearEarthObject
import org.cosmosatlas.core.model.SolarWavelength
import org.cosmosatlas.feature.solar.viewmodel.SolarIntent
import org.cosmosatlas.feature.solar.viewmodel.SolarViewModel

@Composable
fun SolarDashboardScreen(
    viewModel: SolarViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBlack),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Title
        item {
            Column {
                Text(
                    text = "HELIOPHYSICS DASHBOARD",
                    style = MaterialTheme.typography.labelSmall,
                    color = StellarAmber
                )
                Text(
                    text = "Live Sun & Space Weather",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }

        // SDO Solar Disk Viewer
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = StellarAmber.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = StellarAmber,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SDO HELIOVIEWER REAL-TIME DISK",
                            style = MaterialTheme.typography.labelSmall,
                            color = StellarAmber
                        )
                    }
                    Text(
                        text = state.selectedWavelength.temperatureRange,
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonCyan
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Wavelength Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SolarWavelength.entries.forEach { wave ->
                        FilterChip(
                            selected = state.selectedWavelength == wave,
                            onClick = { viewModel.onIntent(SolarIntent.OnWavelengthSelected(wave)) },
                            label = { Text(wave.channelName) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = StellarAmber,
                                selectedLabelColor = SpaceBlack,
                                containerColor = SpaceDarkSurface,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Helioviewer SDO Disk Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = state.solarImageUrl,
                        contentDescription = "Solar Disk",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${state.selectedWavelength.primaryIon} • Target: ${state.selectedWavelength.targetRegion}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        // NOAA Space Weather Telemetry
        item {
            state.spaceWeather?.let { weather ->
                GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "NOAA SWPC SPACE WEATHER INDICES",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonCyan
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TelemetryChip(
                            label = "Solar Wind",
                            value = String.format("%.1f", weather.solarWindSpeedKmS),
                            unit = "km/s",
                            modifier = Modifier.weight(1f)
                        )
                        TelemetryChip(
                            label = "Density",
                            value = String.format("%.1f", weather.solarWindDensityProtonsCm3),
                            unit = "p/cm³",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TelemetryChip(
                            label = "Planetary Kp Index",
                            value = String.format("%.2f", weather.geomagneticKpIndex),
                            unit = "Kp",
                            accentColor = if (weather.geomagneticKpIndex >= 5.0) SupernovaRed else AuroraGreen,
                            modifier = Modifier.weight(1f)
                        )
                        TelemetryChip(
                            label = "Storm Status",
                            value = weather.stormCategory,
                            modifier = Modifier.weight(1f),
                            accentColor = AuroraGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TelemetryChip(
                            label = "Solar Flare Alert",
                            value = weather.solarFlareStatus,
                            accentColor = StellarAmber,
                            modifier = Modifier.weight(1f)
                        )
                        TelemetryChip(
                            label = "Sunspot Count",
                            value = "${weather.sunspotNumber}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // CNEOS Close Approaches Header
        item {
            Text(
                text = "NEAR-EARTH OBJECTS (CLOSE APPROACHES)",
                style = MaterialTheme.typography.labelSmall,
                color = ElectricBlue
            )
        }

        // CNEOS NEO list
        items(state.nearEarthObjects) { neo ->
            NearEarthObjectItem(neo)
        }
    }
}

@Composable
private fun NearEarthObjectItem(neo: NearEarthObject) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (neo.isPotentiallyHazardous) SupernovaRed.copy(alpha = 0.6f) else SpaceBorder
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = neo.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    if (neo.isPotentiallyHazardous) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "PHA",
                            tint = SupernovaRed,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text(
                    text = "Close Approach: ${neo.closeApproachDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${String.format("%.3f", neo.missDistanceAu)} AU",
                    style = MaterialTheme.typography.titleMedium,
                    color = NeonCyan
                )
                Text(
                    text = "${String.format("%.1f", neo.relativeVelocityKmS)} km/s",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }
    }
}
