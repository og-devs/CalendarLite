package com.example.calendario.ui.screen.item

import com.example.calendario.data.Item
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ItemEntryUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val showingDatePicker: Boolean = false,
    val showingStartTimePicker: Boolean = false,
    val showingEndTimePicker: Boolean = false,
)

data class ItemDetails(
    val id: Int = 0,
    val description: String = "",
    val startDate: LocalDateTime = LocalDateTime.now(),
    val endTime: String? = null,
) {

    fun startDay(): String = DateTimeFormatter.ofPattern("EEEE, MMMM dd").format(startDate)

    fun time(): String {
        val startTime = DateTimeFormatter.ofPattern("HH:mm").format(startDate)
        return if (endTime == null) startTime else "$startTime - $endTime"
    }

    fun toItemEntity(): Item = Item(id, description, startDate, endTime)
}