package com.example.calendario.ui.screen.item.edit

import androidx.compose.foundation.layout.padding
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
import com.example.calendario.ui.screen.item.ItemEntryBody
import com.example.calendario.ui.theme.CalendarIOTheme

import kotlinx.coroutines.launch

object ItemEditDestination : NavigationDestination {
    override val route = "item_edit"
    override val titleRes = R.string.edit_item_title
    const val ITEM_ID_ARG = "itemId"
    val routeWithArgs = "$route/{$ITEM_ID_ARG}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ItemEditViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CalendarTopAppBar(
                title = stringResource(ItemEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
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
                    viewModel.updateItem()
                    navigateBack()
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemEditScreenPreview() {
    CalendarIOTheme {
        ItemEditScreen(navigateBack = {  }, onNavigateUp = {  })
    }
}