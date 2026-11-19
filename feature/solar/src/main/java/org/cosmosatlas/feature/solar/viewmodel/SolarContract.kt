package org.cosmosatlas.feature.solar.viewmodel

import org.cosmosatlas.core.model.NearEarthObject
import org.cosmosatlas.core.model.SolarWavelength
import org.cosmosatlas.core.model.SpaceWeatherTelemetry

data class SolarUiState(
    val selectedWavelength: SolarWavelength = SolarWavelength.AIA_171,
    val solarImageUrl: String = "",
    val spaceWeather: SpaceWeatherTelemetry? = null,
    val nearEarthObjects: List<NearEarthObject> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface SolarIntent {
    data class OnWavelengthSelected(val wavelength: SolarWavelength) : SolarIntent
    data object OnRefresh : SolarIntent
}
