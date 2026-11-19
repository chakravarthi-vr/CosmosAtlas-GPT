package org.cosmosatlas.feature.orrery.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.cosmosatlas.core.designsystem.theme.GlassBackground
import org.cosmosatlas.core.designsystem.theme.NeonCyan
import org.cosmosatlas.core.designsystem.theme.SpaceBlack
import org.cosmosatlas.core.designsystem.theme.SpaceDarkSurface
import org.cosmosatlas.core.designsystem.theme.StellarAmber
import org.cosmosatlas.core.designsystem.theme.TextPrimary
import org.cosmosatlas.core.designsystem.theme.TextSecondary
import org.cosmosatlas.feature.orrery.viewmodel.OrreryIntent
import org.cosmosatlas.feature.orrery.viewmodel.OrreryViewModel

@Composable
fun OrreryScreen(
    viewModel: OrreryViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    Box(modifier = modifier.fillMaxSize().background(SpaceBlack)) {
        if (state.isEyesOnSolarSystemMode) {
            // NASA "Eyes on the Solar System" WebGL Interactive View
            NasaEyesWebView()
        } else {
            // Native Keplerian & JPL Horizons 2D/3D Canvas Engine
            SolarSystemCanvas(
                projectedBodies = state.projectedBodies,
                zoomScale = state.zoomScale,
                panOffsetX = state.panOffsetX,
                panOffsetY = state.panOffsetY,
                onBodyClicked = { bodyId ->
                    viewModel.onIntent(OrreryIntent.OnBodySelected(bodyId))
                },
                onTransform = { zoom, dx, dy ->
                    viewModel.onIntent(OrreryIntent.OnZoomChanged(zoom))
                    viewModel.onIntent(OrreryIntent.OnPanChanged(dx, dy))
                }
            )
        }

        // Top Floating HUD: Date & Mode Switcher
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .align(Alignment.TopCenter),
            color = GlassBackground,
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SOLAR SYSTEM ORRERY",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonCyan
                    )
                    Text(
                        text = state.dateString,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                }

                ElevatedButton(
                    onClick = {
                        viewModel.onIntent(OrreryIntent.OnToggleEyesMode(!state.isEyesOnSolarSystemMode))
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = if (state.isEyesOnSolarSystemMode) StellarAmber else SpaceDarkSurface,
                        contentColor = if (state.isEyesOnSolarSystemMode) SpaceBlack else NeonCyan
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ViewInAr,
                        contentDescription = "Mode Switch",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = if (state.isEyesOnSolarSystemMode) "2D Canvas" else "NASA Eyes 3D")
                }
            }
        }

        // Bottom Controls HUD: Time Scrubber, Speed & Reset
        if (!state.isEyesOnSolarSystemMode) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                color = GlassBackground,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Time-Scrubber Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "-5 Years",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Text(
                            text = "TIME SCRUBBER (ORBIT PROPAGATOR)",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonCyan
                        )
                        Text(
                            text = "+5 Years",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }

                    Slider(
                        value = sliderPosition,
                        onValueChange = {
                            sliderPosition = it
                            // Map -1.0..1.0 to -1826..+1826 days (~5 years)
                            viewModel.onIntent(OrreryIntent.OnTimeScrubbed(it.toDouble() * 1826.25))
                        },
                        valueRange = -1f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonCyan,
                            activeTrackColor = NeonCyan,
                            inactiveTrackColor = Color(0x3300E5FF)
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Media Controls & Speed Multipliers
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    viewModel.onIntent(OrreryIntent.OnTogglePlay(!state.isPlaying))
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(SpaceDarkSurface, CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play/Pause",
                                    tint = NeonCyan
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    viewModel.onIntent(OrreryIntent.OnResetView)
                                    sliderPosition = 0f
                                    viewModel.onIntent(OrreryIntent.OnTimeScrubbed(0.0))
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(SpaceDarkSurface, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RestartAlt,
                                    contentDescription = "Reset View",
                                    tint = TextSecondary
                                )
                            }
                        }

                        // Speed toggles: 1x, 10x, 100x
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            SpeedChip(
                                label = "1x",
                                isSelected = state.simulationSpeedDaysPerSec == 1.0f,
                                onClick = { viewModel.onIntent(OrreryIntent.OnSpeedChanged(1.0f)) }
                            )
                            SpeedChip(
                                label = "10x",
                                isSelected = state.simulationSpeedDaysPerSec == 10.0f,
                                onClick = { viewModel.onIntent(OrreryIntent.OnSpeedChanged(10.0f)) }
                            )
                            SpeedChip(
                                label = "100x",
                                isSelected = state.simulationSpeedDaysPerSec == 100.0f,
                                onClick = { viewModel.onIntent(OrreryIntent.OnSpeedChanged(100.0f)) }
                            )
                        }
                    }
                }
            }
        }

        // Celestial Body Dossier Bottom Sheet
        CelestialDossierSheet(
            dossier = state.selectedDossier,
            isLoading = state.isDossierLoading,
            errorMessage = state.dossierError,
            onDismiss = { viewModel.onIntent(OrreryIntent.OnDismissDossier) }
        )
    }
}

@Composable
private fun SpeedChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) NeonCyan else SpaceDarkSurface
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) SpaceBlack else TextPrimary
        )
    }
}
