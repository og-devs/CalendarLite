package com.example.calendario.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calendario.R
import com.example.calendario.ui.navigation.NavigationDestination
import com.example.calendario.ui.screen.CalendarTopAppBar
import com.example.calendario.ui.screen.item.ItemDetails
import com.example.calendario.ui.theme.CalendarIOTheme
import java.time.LocalDateTime


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lazyListState =
        if (homeUiState.itemDetailsList.isEmpty()) {
            rememberLazyListState()
        } else {
            rememberLazyListState(initialFirstVisibleItemIndex = homeUiState.initialFirstVisibleItemIndex)
        }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CalendarTopAppBar(
                title = stringResource(R.string.app_name),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            lazyListState = lazyListState,
            itemDetailsList = homeUiState.itemDetailsList,
            onItemClick = navigateToItemUpdate,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun HomeBody(
    itemDetailsList: List<ItemDetails>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = LazyListState()
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        if (itemDetailsList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            CalendarList(
                lazyListState = lazyListState,
                itemDetailsList = itemDetailsList,
                onItemClick = { onItemClick(it.id) },
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun CalendarList(
    lazyListState: LazyListState,
    itemDetailsList: List<ItemDetails>,
    onItemClick: (ItemDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(state = lazyListState, modifier = modifier) {
        itemsIndexed(items = itemDetailsList) { index, item ->
            val withNewDate = index == 0
                    || itemDetailsList[index].startDay() != itemDetailsList[index-1].startDay()
            CalendarItem(
                withNewDate = withNewDate,
                itemDetails = item,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onItemClick(item) })
        }
    }
}

@Composable
private fun CalendarItem(
    itemDetails: ItemDetails,
    modifier: Modifier = Modifier,
    withNewDate: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (withNewDate) {
            Text(
                text = itemDetails.startDay(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))
            )
        }
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation))
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_large)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                Text(
                    text = itemDetails.time(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = itemDetails.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    CalendarIOTheme {
        HomeBody(listOf(
            ItemDetails(
                id = 1,
                description = "Meeting with Avi and Amit",
                startDate = LocalDateTime.now()
            ),
            ItemDetails(
                id = 2,
                description = "Meeting with Avi and Amit",
                startDate = LocalDateTime.now()
            ),
            ItemDetails(
                id = 3,
                description = "Meeting with Avi and Amit",
                startDate = LocalDateTime.now()
            )
        ), onItemClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
    CalendarIOTheme {
        HomeBody(listOf(
        ), onItemClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun InventoryEventItemPreview() {
    CalendarIOTheme {
        CalendarItem(
            ItemDetails(
                id = 1,
                description = "Meeting with Avi and Amit",
                startDate = LocalDateTime.now()
            )
        )
    }
}