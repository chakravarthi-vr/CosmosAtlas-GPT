package org.cosmosatlas.feature.catalog.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.cosmosatlas.core.astromath.AstrophysicsMath
import org.cosmosatlas.core.designsystem.component.GlassmorphicCard
import org.cosmosatlas.core.designsystem.component.TelemetryChip
import org.cosmosatlas.core.designsystem.theme.DeepViolet
import org.cosmosatlas.core.designsystem.theme.ElectricBlue
import org.cosmosatlas.core.designsystem.theme.NeonCyan
import org.cosmosatlas.core.designsystem.theme.SpaceBlack
import org.cosmosatlas.core.designsystem.theme.SpaceBorder
import org.cosmosatlas.core.designsystem.theme.SpaceDarkSurface
import org.cosmosatlas.core.designsystem.theme.StellarAmber
import org.cosmosatlas.core.designsystem.theme.SupernovaRed
import org.cosmosatlas.core.designsystem.theme.TextMuted
import org.cosmosatlas.core.designsystem.theme.TextPrimary
import org.cosmosatlas.core.designsystem.theme.TextSecondary
import org.cosmosatlas.core.model.CelestialCategory
import org.cosmosatlas.core.model.DeepSpaceObject
import org.cosmosatlas.feature.catalog.viewmodel.WavelengthTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeepSpaceDetailDialog(
    obj: DeepSpaceObject?,
    selectedTab: WavelengthTab,
    onTabSelected: (WavelengthTab) -> Unit,
    onDismiss: () -> Unit
) {
    if (obj == null) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SpaceDarkSurface,
        contentColor = TextPrimary,
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = obj.name,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                    Text(
                        text = "${obj.catalogDesignation} • ${obj.category.name.replace("_", " ")}",
                        style = MaterialTheme.typography.labelSmall,
                        color = StellarAmber
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Multi-Wavelength Imagery Tabs (Optical, Infrared, X-Ray)
            TabRow(
                selectedTabIndex = selectedTab.ordinal,
                containerColor = SpaceBlack,
                contentColor = NeonCyan,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                        color = NeonCyan
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == WavelengthTab.OPTICAL,
                    onClick = { onTabSelected(WavelengthTab.OPTICAL) },
                    text = { Text("OPTICAL", style = MaterialTheme.typography.labelSmall) }
                )
                Tab(
                    selected = selectedTab == WavelengthTab.INFRARED,
                    onClick = { onTabSelected(WavelengthTab.INFRARED) },
                    text = { Text("INFRARED", style = MaterialTheme.typography.labelSmall) }
                )
                Tab(
                    selected = selectedTab == WavelengthTab.XRAY,
                    onClick = { onTabSelected(WavelengthTab.XRAY) },
                    text = { Text("X-RAY", style = MaterialTheme.typography.labelSmall) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val currentImageUrl = when (selectedTab) {
                WavelengthTab.OPTICAL -> obj.imagery.opticalUrl
                WavelengthTab.INFRARED -> obj.imagery.infraredUrl
                WavelengthTab.XRAY -> obj.imagery.xrayUrl
            }

            val currentWavelengthDesc = when (selectedTab) {
                WavelengthTab.OPTICAL -> obj.imagery.opticalDescription
                WavelengthTab.INFRARED -> obj.imagery.infraredDescription
                WavelengthTab.XRAY -> obj.imagery.xrayDescription
            }

            // Image Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SpaceBlack)
            ) {
                AsyncImage(
                    model = currentImageUrl,
                    contentDescription = obj.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currentWavelengthDesc,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // SIMBAD Astrometric Parameters
            Text(
                text = "SIMBAD ASTROMETRIC TELEMETRY",
                style = MaterialTheme.typography.labelSmall,
                color = ElectricBlue
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TelemetryChip(
                    label = "Right Ascension",
                    value = obj.rightAscension,
                    modifier = Modifier.weight(1f)
                )
                TelemetryChip(
                    label = "Declination",
                    value = obj.declination,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TelemetryChip(
                    label = "Constellation",
                    value = obj.constellation,
                    modifier = Modifier.weight(1f)
                )
                TelemetryChip(
                    label = "Spectral Class",
                    value = obj.spectralClassification.take(12),
                    modifier = Modifier.weight(1f),
                    accentColor = StellarAmber
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TelemetryChip(
                    label = "Distance",
                    value = if (obj.distanceLightYears > 1e6) {
                        String.format("%.2f Mly", obj.distanceLightYears / 1e6)
                    } else {
                        String.format("%.0f ly", obj.distanceLightYears)
                    },
                    modifier = Modifier.weight(1f)
                )
                TelemetryChip(
                    label = "Apparent Mag",
                    value = String.format("%.2f", obj.apparentMagnitude),
                    modifier = Modifier.weight(1f)
                )
                obj.redshiftZ?.let { z ->
                    TelemetryChip(
                        label = "Redshift z",
                        value = String.format("%.4f", z),
                        modifier = Modifier.weight(1f),
                        accentColor = SupernovaRed
                    )
                }
            }

            // Computed Relativistic / Astrophysical Mechanics
            if (obj.category == CelestialCategory.BLACK_HOLE) {
                Spacer(modifier = Modifier.height(16.dp))
                val massKg = if (obj.id == "m87_blackhole") 6.5e9 * AstrophysicsMath.SOLAR_MASS_KG else 4.15e6 * AstrophysicsMath.SOLAR_MASS_KG
                val rsKm = AstrophysicsMath.calculateSchwarzschildRadius(massKg) / 1000.0

                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = DeepViolet
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = DeepViolet,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "GENERAL RELATIVISTIC EVENT HORIZON",
                            style = MaterialTheme.typography.labelSmall,
                            color = DeepViolet
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Schwarzschild Event Horizon Radius (r_s = 2GM/c²): ~${String.format("%,.0f", rsKm)} km",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Astrophysical Breakdown
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Science,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ASTROPHYSICAL PROCESSES & MECHANISMS",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonCyan
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = obj.physicalProcessesDescription,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
