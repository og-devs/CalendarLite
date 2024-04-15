package com.example.calendario.ui.screen.home

import com.example.calendario.ui.screen.item.ItemDetails

data class HomeUiState(
    val itemDetailsList: List<ItemDetails> = emptyList(),
    val initialFirstVisibleItemIndex: Int = 0
)