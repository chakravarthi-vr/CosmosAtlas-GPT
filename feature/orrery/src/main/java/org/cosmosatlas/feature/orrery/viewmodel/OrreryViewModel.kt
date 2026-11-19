package org.cosmosatlas.feature.orrery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.cosmosatlas.core.astromath.AstrophysicsMath
import org.cosmosatlas.core.astromath.SolarSystemCatalog
import org.cosmosatlas.core.network.AstrophysicsRepository
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class OrreryViewModel @Inject constructor(
    private val repository: AstrophysicsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrreryUiState())
    val uiState: StateFlow<OrreryUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<OrrerySideEffect>()
    val effects: SharedFlow<OrrerySideEffect> = _effects.asSharedFlow()

    private var simulationJob: Job? = null
    private var baseJd = 2451545.0 // Base Julian Date

    init {
        // Initialize to current real-world Julian date
        val now = LocalDate.now()
        baseJd = AstrophysicsMath.calendarToJulianDate(
            year = now.year,
            month = now.monthValue,
            day = now.dayOfMonth,
            hour = 12,
            minute = 0
        )
        _uiState.update {
            it.copy(
                currentJulianDate = baseJd,
                dateString = now.toString()
            )
        }
        recalculateOrbits(baseJd)
        startSimulationTicker()
    }

    fun onIntent(intent: OrreryIntent) {
        when (intent) {
            is OrreryIntent.OnBodySelected -> loadBodyDossier(intent.bodyId)
            OrreryIntent.OnDismissDossier -> _uiState.update { it.copy(selectedBodyId = null, selectedDossier = null) }
            is OrreryIntent.OnTimeScrubbed -> {
                val newJd = baseJd + intent.daysOffset
                updateDateAndOrbits(newJd)
            }
            is OrreryIntent.OnTogglePlay -> {
                _uiState.update { it.copy(isPlaying = intent.isPlaying) }
                if (intent.isPlaying) startSimulationTicker() else simulationJob?.cancel()
            }
            is OrreryIntent.OnSpeedChanged -> _uiState.update { it.copy(simulationSpeedDaysPerSec = intent.speedMultiplier) }
            is OrreryIntent.OnZoomChanged -> _uiState.update {
                val newZoom = (it.zoomScale * intent.zoomDelta).coerceIn(0.15f, 15.0f)
                it.copy(zoomScale = newZoom)
            }
            is OrreryIntent.OnPanChanged -> _uiState.update {
                it.copy(panOffsetX = it.panOffsetX + intent.dx, panOffsetY = it.panOffsetY + intent.dy)
            }
            OrreryIntent.OnResetView -> _uiState.update {
                it.copy(zoomScale = 1.0f, panOffsetX = 0f, panOffsetY = 0f)
            }
            is OrreryIntent.OnToggleEyesMode -> _uiState.update {
                it.copy(isEyesOnSolarSystemMode = intent.enabled)
            }
        }
    }

    private fun startSimulationTicker() {
        simulationJob?.cancel()
        simulationJob = viewModelScope.launch {
            val frameRateMs = 16L // ~60 fps
            while (isActive) {
                delay(frameRateMs)
                if (_uiState.value.isPlaying) {
                    val daysPerFrame = (_uiState.value.simulationSpeedDaysPerSec / 1000.0) * frameRateMs
                    val nextJd = _uiState.value.currentJulianDate + daysPerFrame
                    updateDateAndOrbits(nextJd)
                }
            }
        }
    }

    private fun updateDateAndOrbits(jd: Double) {
        // Approximate Gregorian date string
        val centuries = AstrophysicsMath.julianCenturiesSinceJ2000(jd)
        val approxYear = 2000 + (centuries * 100).toInt()
        val dayOfYear = ((jd - 2451545.0) % 365.25).toInt()
        val dateDisplay = "JD ${String.format("%.2f", jd)} (Yr ~$approxYear)"

        _uiState.update {
            it.copy(
                currentJulianDate = jd,
                dateString = dateDisplay
            )
        }
        recalculateOrbits(jd)
    }

    private fun recalculateOrbits(jd: Double) {
        val projected = SolarSystemCatalog.ALL_BODIES.map { body ->
            val pos = AstrophysicsMath.computeHeliocentricCoordinates(body.orbit, jd)
            ProjectedPlanet(
                data = body,
                positionAU = pos
            )
        }
        _uiState.update { it.copy(projectedBodies = projected) }
    }

    private fun loadBodyDossier(bodyId: String) {
        _uiState.update { it.copy(selectedBodyId = bodyId, isDossierLoading = true, dossierError = null) }
        viewModelScope.launch {
            repository.getSolarSystemBodyDossier(bodyId)
                .catch { e ->
                    _uiState.update { it.copy(isDossierLoading = false, dossierError = e.localizedMessage) }
                }
                .collect { dossier ->
                    _uiState.update { it.copy(selectedDossier = dossier, isDossierLoading = false) }
                }
        }
    }
}
