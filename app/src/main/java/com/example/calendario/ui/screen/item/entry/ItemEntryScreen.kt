package com.example.calendario.ui.screen.item.entry

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calendario.R
import com.example.calendario.ui.navigation.NavigationDestination
import com.example.calendario.ui.screen.CalendarTopAppBar
import com.example.calendario.ui.screen.item.ItemDetails
import com.example.calendario.ui.screen.item.ItemEntryBody
import com.example.calendario.ui.screen.item.ItemEntryUiState
import com.example.calendario.ui.theme.CalendarIOTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime

object ItemEntryDestination : NavigationDestination {
    override val route = "item_entry"
    override val titleRes = R.string.item_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: ItemEntryViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CalendarTopAppBar(
                title = stringResource(ItemEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        ItemEntryBody(
            itemEntryUiState = viewModel.itemEntryUiState,
            onItemValueChange = viewModel::updateUiState,
            onDateClick = viewModel::showHideDatePicker,
            onStartTimeClick = viewModel::showHideStartTimePicker,
            onEndTimeClick = viewModel::showHideEndTimePicker,
            onDateSelection = viewModel::setDate,
            onStartTimeSelection = viewModel::setStartTime,
            onEndTimeSelection = viewModel::setEndTime,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}



@Preview(showBackground = true)
@Composable
private fun ItemEntryScreenPreview() {
    CalendarIOTheme {
        ItemEntryBody(itemEntryUiState = ItemEntryUiState(
            itemDetails = ItemDetails(
                id = 1,
                description = "Meeting with Avi and Amit",
                startDate = LocalDateTime.now()
            )
        ), onDateClick = {}, onStartTimeClick = {}, onEndTimeClick = {},
            onDateSelection = {}, onItemValueChange = {}, onStartTimeSelection = { _, _ -> },
            onEndTimeSelection = { _, _ -> }, onSaveClick = {})
    }
}