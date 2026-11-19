package org.cosmosatlas.feature.catalog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.cosmosatlas.core.model.CelestialCategory
import org.cosmosatlas.core.model.DeepSpaceObject
import org.cosmosatlas.core.network.AstrophysicsRepository
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val repository: AstrophysicsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState(isLoading = true))
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init {
        loadCatalog()
    }

    fun onIntent(intent: CatalogIntent) {
        when (intent) {
            is CatalogIntent.OnCategorySelected -> {
                _uiState.update { it.copy(selectedCategory = intent.category) }
                applyFilter()
            }
            is CatalogIntent.OnSearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = intent.query) }
                applyFilter()
            }
            is CatalogIntent.OnObjectSelected -> {
                _uiState.update { it.copy(selectedObject = intent.obj, selectedWavelengthTab = WavelengthTab.OPTICAL) }
            }
            is CatalogIntent.OnWavelengthTabSelected -> {
                _uiState.update { it.copy(selectedWavelengthTab = intent.tab) }
            }
            CatalogIntent.OnRefresh -> loadCatalog()
        }
    }

    private fun loadCatalog() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            repository.getDeepSpaceCatalog()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
                }
                .collect { list ->
                    _uiState.update {
                        it.copy(
                            objects = list,
                            isLoading = false
                        )
                    }
                    applyFilter()
                }
        }
    }

    private fun applyFilter() {
        val all = _uiState.value.objects
        val cat = _uiState.value.selectedCategory
        val query = _uiState.value.searchQuery.trim().lowercase()

        val filtered = all.filter { obj ->
            val matchesCategory = cat == null || obj.category == cat
            val matchesQuery = query.isEmpty() ||
                    obj.name.lowercase().contains(query) ||
                    obj.catalogDesignation.lowercase().contains(query) ||
                    obj.constellation.lowercase().contains(query)
            matchesCategory && matchesQuery
        }

        _uiState.update { it.copy(filteredObjects = filtered) }
    }
}
