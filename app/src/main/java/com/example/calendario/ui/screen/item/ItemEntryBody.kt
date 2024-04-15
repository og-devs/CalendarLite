package com.example.calendario.ui.screen.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.calendario.R
import java.util.concurrent.TimeUnit

@Composable
fun ItemEntryBody(
    itemEntryUiState: ItemEntryUiState,
    onItemValueChange: (ItemDetails) -> Unit,
    onDateClick: () -> Unit,
    onStartTimeClick: () -> Unit,
    onEndTimeClick: () -> Unit,
    onDateSelection: (Long) -> Unit,
    onStartTimeSelection: (Int, Int) -> Unit,
    onEndTimeSelection: (Int, Int) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        ItemInputForm(
            itemEntryUiState = itemEntryUiState,
            onItemValueChange = onItemValueChange,
            onDateClick = onDateClick,
            onToggleStartTime = onStartTimeClick,
            onToggleEndTime = onEndTimeClick,
            onDateSelection = onDateSelection,
            onStartTimeSelection = onStartTimeSelection,
            onEndTimeSelection = onEndTimeSelection,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

@Composable
fun ItemInputForm(
    itemEntryUiState: ItemEntryUiState,
    modifier: Modifier = Modifier,
    onItemValueChange: (ItemDetails) -> Unit = {},
    onDateClick: () -> Unit,
    onToggleStartTime: () -> Unit,
    onToggleEndTime: () -> Unit,
    onDateSelection: (Long) -> Unit,
    onStartTimeSelection: (Int, Int) -> Unit,
    onEndTimeSelection: (Int, Int) -> Unit
) {
    DateTimePickers(
        itemEntryUiState = itemEntryUiState,
        onDateClick = onDateClick,
        onToggleStartTime = onToggleStartTime,
        onToggleEndTime = onToggleEndTime,
        onDateSelection = onDateSelection,
        onStartTimeSelection = onStartTimeSelection,
        onEndTimeSelection = onEndTimeSelection
    )
    Column(
        modifier = modifier
    ) {
        TextButton(
            onClick = onDateClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_large)))
                Text(
                    text = itemEntryUiState.itemDetails.startDay(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        TextButton(
            onClick = onToggleStartTime,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_large)))
                Text(
                    text = itemEntryUiState.itemDetails.time(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))
        OutlinedTextField(
            value = itemEntryUiState.itemDetails.description,
            onValueChange = { onItemValueChange(itemEntryUiState.itemDetails.copy(description = it)) },
            label = { Text(stringResource(R.string.item_description)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickers(
    itemEntryUiState: ItemEntryUiState,
    onDateClick: () -> Unit,
    onToggleStartTime: () -> Unit,
    onToggleEndTime: () -> Unit,
    onDateSelection: (Long) -> Unit,
    onStartTimeSelection: (Int, Int) -> Unit,
    onEndTimeSelection: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val timePickerState = rememberTimePickerState()
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis + TimeUnit.DAYS.toMillis(1) > System.currentTimeMillis()
        }
    })
    if (itemEntryUiState.showingDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                datePickerState.selectedDateMillis?.let { onDateSelection(it) }
                    ?: onDateClick()
            },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { onDateSelection(it) }
                            ?: onDateClick()
                    }
                ) {
                    Text(text = "OK")
                }
            },
            modifier = modifier.padding(dimensionResource(R.dimen.padding_large))
        ) {
            DatePicker(state = datePickerState)
        }
    }
    if (itemEntryUiState.showingStartTimePicker) {
        Dialog(onDismissRequest = onToggleStartTime) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TimePicker(state = timePickerState, modifier = Modifier.fillMaxWidth())
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        onStartTimeSelection(timePickerState.hour, timePickerState.minute)
                    }) {
                        Text(text = "OK")
                    }
                    Button(onClick = {
                        onStartTimeSelection(timePickerState.hour, timePickerState.minute)
                        onToggleEndTime()
                    }) {
                        Text(text = "Pick End Time")
                    }
                }
            }

        }

    }
    if (itemEntryUiState.showingEndTimePicker) {
        Dialog(onDismissRequest = onToggleEndTime) {
            Column {
                TimePicker(state = timePickerState)
                Button(onClick = {
                    onEndTimeSelection(timePickerState.hour, timePickerState.minute)
                }) {
                    Text(text = "OK")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemEntryBodyPreview() {
    ItemEntryBody(
        itemEntryUiState = ItemEntryUiState(),
        onItemValueChange = {},
        onDateClick = {},
        onStartTimeClick = {},
        onEndTimeClick = {},
        onDateSelection = {},
        onStartTimeSelection = { _, _ -> },
        onEndTimeSelection = { _, _ -> },
        onSaveClick = {})
}