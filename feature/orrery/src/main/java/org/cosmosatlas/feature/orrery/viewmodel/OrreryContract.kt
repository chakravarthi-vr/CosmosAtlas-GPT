package org.cosmosatlas.feature.orrery.viewmodel

import org.cosmosatlas.core.astromath.CelestialBodyOrbitData
import org.cosmosatlas.core.astromath.SolarSystemCatalog
import org.cosmosatlas.core.astromath.Vector3D
import org.cosmosatlas.core.model.CelestialBodyDossier

data class ProjectedPlanet(
    val data: CelestialBodyOrbitData,
    val positionAU: Vector3D,
    val screenX: Float = 0f,
    val screenY: Float = 0f,
    val screenRadius: Float = 4f
)

data class OrreryUiState(
    val currentJulianDate: Double = 2451545.0, // default J2000
    val dateString: String = "2026-11-19",
    val isPlaying: Boolean = true,
    val simulationSpeedDaysPerSec: Float = 5.0f,
    val zoomScale: Float = 1.0f,
    val panOffsetX: Float = 0f,
    val panOffsetY: Float = 0f,
    val selectedBodyId: String? = null,
    val selectedDossier: CelestialBodyDossier? = null,
    val isDossierLoading: Boolean = false,
    val dossierError: String? = null,
    val projectedBodies: List<ProjectedPlanet> = emptyList(),
    val isEyesOnSolarSystemMode: Boolean = false // WebGL NASA Eyes embed toggle
)

sealed interface OrreryIntent {
    data class OnBodySelected(val bodyId: String) : OrreryIntent
    data object OnDismissDossier : OrreryIntent
    data class OnTimeScrubbed(val daysOffset: Double) : OrreryIntent
    data class OnTogglePlay(val isPlaying: Boolean) : OrreryIntent
    data class OnSpeedChanged(val speedMultiplier: Float) : OrreryIntent
    data class OnZoomChanged(val zoomDelta: Float) : OrreryIntent
    data class OnPanChanged(val dx: Float, val dy: Float) : OrreryIntent
    data object OnResetView : OrreryIntent
    data class OnToggleEyesMode(val enabled: Boolean) : OrreryIntent
}

sealed interface OrrerySideEffect {
    data class ShowToast(val message: String) : OrrerySideEffect
}
