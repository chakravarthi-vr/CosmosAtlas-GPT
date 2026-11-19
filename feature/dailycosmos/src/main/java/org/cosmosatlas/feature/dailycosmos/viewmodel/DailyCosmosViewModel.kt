package org.cosmosatlas.feature.dailycosmos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.cosmosatlas.core.model.ApodRecord
import org.cosmosatlas.core.network.AstrophysicsRepository
import javax.inject.Inject

@HiltViewModel
class DailyCosmosViewModel @Inject constructor(
    private val repository: AstrophysicsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyCosmosUiState(isLoading = true))
    val uiState: StateFlow<DailyCosmosUiState> = _uiState.asStateFlow()

    init {
        loadDailyApod()
        observeBookmarks()
    }

    fun onIntent(intent: DailyCosmosIntent) {
        when (intent) {
            is DailyCosmosIntent.OnToggleBookmark -> {
                val newStatus = !intent.apod.isBookmarked
                viewModelScope.launch {
                    repository.toggleApodBookmark(intent.apod.date, newStatus)
                    if (_uiState.value.currentApod?.date == intent.apod.date) {
                        _uiState.update { it.copy(currentApod = it.currentApod?.copy(isBookmarked = newStatus)) }
                    }
                }
            }
            is DailyCosmosIntent.OnSwitchTab -> {
                _uiState.update { it.copy(showingBookmarksTab = intent.showBookmarks) }
            }
            is DailyCosmosIntent.OnToggleZoomModal -> {
                _uiState.update { it.copy(isZoomModalOpen = intent.isOpen) }
            }
            DailyCosmosIntent.OnRefresh -> loadDailyApod(forceRefresh = true)
        }
    }

    private fun loadDailyApod(forceRefresh: Boolean = false) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            repository.getApodToday(forceRefresh = forceRefresh)
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
                }
                .collect { apod ->
                    _uiState.update { it.copy(currentApod = apod, isLoading = false) }
                }
        }
    }

    private fun observeBookmarks() {
        viewModelScope.launch {
            repository.getBookmarkedApods()
                .catch { /* ignore */ }
                .collect { bookmarks ->
                    _uiState.update { it.copy(bookmarkedApods = bookmarks) }
                }
        }
    }
}
