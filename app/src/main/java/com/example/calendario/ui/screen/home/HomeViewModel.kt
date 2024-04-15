package com.example.calendario.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendario.data.Item
import com.example.calendario.data.ItemsRepository
import com.example.calendario.ui.screen.item.ItemDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(itemsRepository: ItemsRepository) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5000L
    }

    private fun Item.toItemDetails(): ItemDetails =
        ItemDetails(id, description, startDate, endTime)

    val homeUiState: StateFlow<HomeUiState> =
        itemsRepository.getAllItemsStream()
            .map { HomeUiState(
                it.map { item -> item.toItemDetails() },
                it.indexOf(it.firstOrNull { item -> isTodayOrAfter(item.startDate) } ?: 0)
            ) }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    private fun isTodayOrAfter(date: LocalDateTime): Boolean {
        val today = LocalDateTime.now()
        if (date.year > today.year) return true
        if (date.year == today.year && date.dayOfYear >= today.dayOfYear) return true
        return false
    }
}