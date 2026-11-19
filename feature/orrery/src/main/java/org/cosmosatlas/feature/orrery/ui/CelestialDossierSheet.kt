package org.cosmosatlas.feature.orrery.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
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
import org.cosmosatlas.core.designsystem.component.GlassmorphicCard
import org.cosmosatlas.core.designsystem.component.TelemetryChip
import org.cosmosatlas.core.designsystem.theme.AuroraGreen
import org.cosmosatlas.core.designsystem.theme.ElectricBlue
import org.cosmosatlas.core.designsystem.theme.NeonCyan
import org.cosmosatlas.core.designsystem.theme.SpaceBlack
import org.cosmosatlas.core.designsystem.theme.SpaceBorder
import org.cosmosatlas.core.designsystem.theme.SpaceDarkSurface
import org.cosmosatlas.core.designsystem.theme.StellarAmber
import org.cosmosatlas.core.designsystem.theme.TextMuted
import org.cosmosatlas.core.designsystem.theme.TextPrimary
import org.cosmosatlas.core.designsystem.theme.TextSecondary
import org.cosmosatlas.core.model.CelestialBodyDossier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CelestialDossierSheet(
    dossier: CelestialBodyDossier?,
    isLoading: Boolean,
    errorMessage: String?,
    onDismiss: () -> Unit
) {
    if (dossier == null && !isLoading && errorMessage == null) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SpaceDarkSurface,
        contentColor = TextPrimary,
        tonalElevation = 8.dp
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = NeonCyan)
            }
            return@ModalBottomSheet
        }

        if (dossier == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage ?: "Failed to load telemetry",
                    color = MaterialTheme.colorScheme.error
                )
            }
            return@ModalBottomSheet
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = dossier.name,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(dossier.telemetry.colorHex)
                    )
                    Text(
                        text = dossier.bodyType.name.replace("_", " "),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
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

            // Real-Time JPL Horizons Telemetry (if available)
            dossier.realTimeEphemeris?.let { ephem ->
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonCyan.copy(alpha = 0.5f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "LIVE JPL HORIZONS EPHEMERIS",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonCyan
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TelemetryChip(
                            label = "Helio Distance",
                            value = String.format("%.3f", ephem.distanceSunAU),
                            unit = "AU"
                        )
                        TelemetryChip(
                            label = "Orbital Velocity",
                            value = String.format("%.2f", ephem.velocityKmS),
                            unit = "km/s",
                            accentColor = AuroraGreen
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Orbital & Physical Telemetry Matrix
            Text(
                text = "PHYSICAL & ORBITAL PARAMETERS",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TelemetryChip(
                    label = "Mean Radius",
                    value = String.format("%.1f", dossier.telemetry.meanRadiusKm),
                    unit = "km"
                )
                TelemetryChip(
                    label = "Surface Gravity",
                    value = String.format("%.2f", dossier.telemetry.surfaceGravityMs2),
                    unit = "m/s²"
                )
                TelemetryChip(
                    label = "Escape Velocity",
                    value = String.format("%.2f", dossier.telemetry.escapeVelocityKmS),
                    unit = "km/s"
                )
                TelemetryChip(
                    label = "Semi-Major Axis",
                    value = String.format("%.3f", dossier.semiMajorAxisAU),
                    unit = "AU"
                )
                TelemetryChip(
                    label = "Eccentricity",
                    value = String.format("%.4f", dossier.eccentricity)
                )
                TelemetryChip(
                    label = "Axial Tilt",
                    value = String.format("%.2f°", dossier.telemetry.axialTiltDeg)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Atmospheric Composition
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "ATMOSPHERIC COMPOSITION",
                    style = MaterialTheme.typography.labelSmall,
                    color = StellarAmber
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = dossier.telemetry.atmosphereSummary,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Multi-Section Scientific Breakdown
            DossierSection(title = "FORMATION & ACCRETION", content = dossier.formation)
            DossierSection(title = "INTERNAL STRUCTURE & CORE", content = dossier.internalStructure)
            DossierSection(title = "MAGNETOSPHERE & DYNAMO", content = dossier.magnetosphere)
            DossierSection(title = "EXPLORATION MISSIONS", content = dossier.explorationHistory)

            // NASA High-Res Imagery Gallery
            if (dossier.galleryImages.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "NASA MISSION IMAGERY ARCHIVE",
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricBlue
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    dossier.galleryImages.forEach { image ->
                        Column(
                            modifier = Modifier
                                .width(220.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SpaceBlack)
                        ) {
                            AsyncImage(
                                model = image.thumbnailUrl,
                                contentDescription = image.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                contentScale = ContentScale.Crop
                            )
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = image.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1,
                                    color = TextPrimary
                                )
                                Text(
                                    text = image.mission,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NeonCyan
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun DossierSection(title: String, content: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Science,
                contentDescription = null,
                tint = NeonCyan,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = NeonCyan
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
    }
}
