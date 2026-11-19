package org.cosmosatlas.feature.dailycosmos.viewmodel

import org.cosmosatlas.core.model.ApodRecord

data class DailyCosmosUiState(
    val currentApod: ApodRecord? = null,
    val bookmarkedApods: List<ApodRecord> = emptyList(),
    val showingBookmarksTab: Boolean = false,
    val isZoomModalOpen: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface DailyCosmosIntent {
    data class OnToggleBookmark(val apod: ApodRecord) : DailyCosmosIntent
    data class OnSwitchTab(val showBookmarks: Boolean) : DailyCosmosIntent
    data class OnToggleZoomModal(val isOpen: Boolean) : DailyCosmosIntent
    data object OnRefresh : DailyCosmosIntent
}
