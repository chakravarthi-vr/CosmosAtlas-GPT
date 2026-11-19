package org.cosmosatlas.feature.catalog.viewmodel

import org.cosmosatlas.core.model.CelestialCategory
import org.cosmosatlas.core.model.DeepSpaceObject

data class CatalogUiState(
    val selectedCategory: CelestialCategory? = null,
    val searchQuery: String = "",
    val objects: List<DeepSpaceObject> = emptyList(),
    val filteredObjects: List<DeepSpaceObject> = emptyList(),
    val selectedObject: DeepSpaceObject? = null,
    val selectedWavelengthTab: WavelengthTab = WavelengthTab.OPTICAL,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

enum class WavelengthTab {
    OPTICAL,
    INFRARED,
    XRAY
}

sealed interface CatalogIntent {
    data class OnCategorySelected(val category: CelestialCategory?) : CatalogIntent
    data class OnSearchQueryChanged(val query: String) : CatalogIntent
    data class OnObjectSelected(val obj: DeepSpaceObject?) : CatalogIntent
    data class OnWavelengthTabSelected(val tab: WavelengthTab) : CatalogIntent
    data object OnRefresh : CatalogIntent
}
