package com.example.calendario.ui.screen.item.entry

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.calendario.data.ItemsRepository
import com.example.calendario.ui.screen.item.ItemDetails
import com.example.calendario.ui.screen.item.ItemEntryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ItemEntryViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    var itemEntryUiState by mutableStateOf(ItemEntryUiState())
        private set

    fun showHideDatePicker() {
        itemEntryUiState = itemEntryUiState.copy(showingDatePicker = !itemEntryUiState.showingDatePicker)
    }

    fun showHideStartTimePicker() {
        itemEntryUiState = itemEntryUiState.copy(showingStartTimePicker = !itemEntryUiState.showingStartTimePicker)
    }

    fun showHideEndTimePicker() {
        itemEntryUiState = itemEntryUiState.copy(showingEndTimePicker = !itemEntryUiState.showingEndTimePicker)
    }

    fun setDate(millis: Long) {
        val startDate = LocalDateTime
            .ofEpochSecond(millis / 1000, 0, ZonedDateTime.now().offset)
        itemEntryUiState = itemEntryUiState.copy(
            itemDetails = itemEntryUiState.itemDetails.copy(startDate = startDate),
            showingDatePicker = false
        )
    }

    fun setStartTime(hour: Int, minute: Int) {
        val currentStartDate = itemEntryUiState.itemDetails.startDate
        val startDate = LocalDateTime.of(
            currentStartDate.year,
            currentStartDate.month,
            currentStartDate.dayOfMonth,
            hour,
            minute
        )
        itemEntryUiState = itemEntryUiState.copy(
            itemDetails = itemEntryUiState.itemDetails.copy(startDate = startDate, endTime = null),
            showingStartTimePicker = false
        )
    }

    fun setEndTime(hour: Int, minute: Int) {
        itemEntryUiState = itemEntryUiState.copy(
            itemDetails = itemEntryUiState.itemDetails.copy(
                endTime = "${hour.toString().padStart(2, '0')}:" +
                        minute.toString().padStart(2, '0')
            ),
            showingEndTimePicker = false
        )
    }

    fun updateUiState(itemDetails: ItemDetails) {
        itemEntryUiState = ItemEntryUiState(itemDetails)
    }

    suspend fun saveItem() {
        itemsRepository.insertItem(itemEntryUiState.itemDetails.toItemEntity())
    }
}