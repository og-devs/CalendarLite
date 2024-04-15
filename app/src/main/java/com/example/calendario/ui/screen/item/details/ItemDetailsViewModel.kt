package com.example.calendario.ui.screen.item.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendario.data.Item
import com.example.calendario.data.ItemsRepository
import com.example.calendario.ui.screen.item.ItemEntryUiState
import com.example.calendario.ui.screen.item.ItemDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ItemDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5000L
    }

    private fun Item.toItemDetails(): ItemDetails = ItemDetails(id, description, startDate, endTime)

    private val itemId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.ITEM_ID_ARG])

    val uiState: StateFlow<ItemEntryUiState> =
        itemsRepository.getItemStream(itemId)
            .filterNotNull()
            .map { ItemEntryUiState(it.toItemDetails()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ItemEntryUiState()
            )

    suspend fun deleteItem() {
        itemsRepository.deleteItem(uiState.value.itemDetails.toItemEntity())
    }
}
