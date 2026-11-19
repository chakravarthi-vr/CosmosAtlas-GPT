package org.cosmosatlas.feature.solar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.cosmosatlas.core.model.SolarWavelength
import org.cosmosatlas.core.network.AstrophysicsRepository
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class SolarViewModel @Inject constructor(
    private val repository: AstrophysicsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SolarUiState(isLoading = true))
    val uiState: StateFlow<SolarUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun onIntent(intent: SolarIntent) {
        when (intent) {
            is SolarIntent.OnWavelengthSelected -> {
                val newUrl = getWavelengthUrl(intent.wavelength)
                _uiState.update {
                    it.copy(
                        selectedWavelength = intent.wavelength,
                        solarImageUrl = newUrl
                    )
                }
            }
            SolarIntent.OnRefresh -> loadData()
        }
    }

    private fun loadData() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        val initialUrl = getWavelengthUrl(_uiState.value.selectedWavelength)
        _uiState.update { it.copy(solarImageUrl = initialUrl) }

        viewModelScope.launch {
            repository.getSpaceWeatherTelemetry()
                .catch { e ->
                    _uiState.update { it.copy(errorMessage = e.localizedMessage) }
                }
                .collect { weather ->
                    _uiState.update { it.copy(spaceWeather = weather) }
                }
        }

        viewModelScope.launch {
            repository.getNearEarthObjects()
                .catch { /* ignore */ }
                .collect { neos ->
                    _uiState.update { it.copy(nearEarthObjects = neos, isLoading = false) }
                }
        }
    }

    private fun getWavelengthUrl(wavelength: SolarWavelength): String {
        val nowIso = Instant.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
        return repository.getHelioviewerImageUrl(wavelength, nowIso)
    }
}
