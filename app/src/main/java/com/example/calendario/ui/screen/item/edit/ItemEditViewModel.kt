package com.example.calendario.ui.screen.item.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendario.data.Item
import com.example.calendario.data.ItemsRepository
import com.example.calendario.ui.screen.item.ItemEntryUiState
import com.example.calendario.ui.screen.item.ItemDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ItemEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private fun Item.toItemDetails(): ItemDetails = ItemDetails(id, description, startDate, endTime)

    private val itemId: Int = checkNotNull(savedStateHandle[ItemEditDestination.ITEM_ID_ARG])

    init {
        viewModelScope.launch {
            itemEntryUiState = ItemEntryUiState(
                itemsRepository
                    .getItemStream(itemId)
                    .filterNotNull()
                    .first()
                    .toItemDetails()
            )
        }
    }

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
        val startDate = LocalDateTime.ofEpochSecond(millis / 1000, 0, ZonedDateTime.now().offset)
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

    suspend fun updateItem() {
        itemsRepository.updateItem(itemEntryUiState.itemDetails.toItemEntity())
    }
}