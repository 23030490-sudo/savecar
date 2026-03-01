package com.savecar.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.savecar.app.R
import com.savecar.app.data.local.entity.MaintenanceRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val MAINTENANCE_TYPES = listOf(
    "Cambio de aceite",
    "Cambio de filtros",
    "Revisión de frenos",
    "Cambio de neumáticos",
    "Revisión general",
    "Cambio de correa de distribución",
    "Alineación y balanceo",
    "Cambio de bujías",
    "Cambio de batería",
    "Revisión de suspensión",
    "Otro"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMaintenanceScreen(
    record: MaintenanceRecord?,
    vehicleId: Long,
    onSave: (MaintenanceRecord) -> Unit,
    onCancel: () -> Unit
) {
    var type by rememberSaveable { mutableStateOf(record?.type ?: MAINTENANCE_TYPES[0]) }
    var customType by rememberSaveable { mutableStateOf("") }
    var dateMillis by rememberSaveable { mutableStateOf(record?.date ?: System.currentTimeMillis()) }
    var mileage by rememberSaveable { mutableStateOf(record?.mileageAtService?.toString() ?: "") }
    var cost by rememberSaveable { mutableStateOf(record?.cost?.let { if (it > 0) it.toString() else "" } ?: "") }
    var workshop by rememberSaveable { mutableStateOf(record?.workshop ?: "") }
    var notes by rememberSaveable { mutableStateOf(record?.notes ?: "") }
    var nextMileage by rememberSaveable { mutableStateOf(record?.nextServiceMileage?.let { if (it > 0) it.toString() else "" } ?: "") }
    var nextDateMillis by rememberSaveable { mutableStateOf(record?.nextServiceDate ?: 0L) }
    var typeDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showNextDatePicker by rememberSaveable { mutableStateOf(false) }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val isEditing = record != null
    val title = if (isEditing) stringResource(R.string.edit_maintenance) else stringResource(R.string.add_maintenance)

    val resolvedType = if (type == "Otro") customType.trim() else type
    val isFormValid = resolvedType.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = typeDropdownExpanded,
                onExpandedChange = { typeDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = type,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.maintenance_type)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeDropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = typeDropdownExpanded,
                    onDismissRequest = { typeDropdownExpanded = false }
                ) {
                    MAINTENANCE_TYPES.forEach { t ->
                        DropdownMenuItem(
                            text = { Text(t) },
                            onClick = {
                                type = t
                                typeDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            if (type == "Otro") {
                OutlinedTextField(
                    value = customType,
                    onValueChange = { customType = it },
                    label = { Text(stringResource(R.string.custom_type)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = dateFormat.format(Date(dateMillis)),
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.service_date)) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    TextButton(onClick = { showDatePicker = true }) {
                        Text(stringResource(R.string.select))
                    }
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = mileage,
                    onValueChange = { mileage = it },
                    label = { Text(stringResource(R.string.mileage_km)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = cost,
                    onValueChange = { cost = it },
                    label = { Text(stringResource(R.string.cost)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }

            OutlinedTextField(
                value = workshop,
                onValueChange = { workshop = it },
                label = { Text(stringResource(R.string.workshop)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text(stringResource(R.string.notes)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )

            Text(
                text = stringResource(R.string.next_service),
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = nextMileage,
                    onValueChange = { nextMileage = it },
                    label = { Text(stringResource(R.string.next_mileage)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = if (nextDateMillis > 0L) dateFormat.format(Date(nextDateMillis)) else "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.next_date)) },
                    modifier = Modifier.weight(1f),
                    trailingIcon = {
                        TextButton(onClick = { showNextDatePicker = true }) {
                            Text(stringResource(R.string.select))
                        }
                    }
                )
            }

            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.cancel))
                }
                Button(
                    onClick = {
                        val r = MaintenanceRecord(
                            id = record?.id ?: 0L,
                            vehicleId = vehicleId,
                            type = resolvedType,
                            date = dateMillis,
                            mileageAtService = mileage.toIntOrNull() ?: 0,
                            cost = cost.toDoubleOrNull() ?: 0.0,
                            workshop = workshop.trim(),
                            notes = notes.trim(),
                            nextServiceMileage = nextMileage.toIntOrNull() ?: 0,
                            nextServiceDate = nextDateMillis
                        )
                        onSave(r)
                    },
                    modifier = Modifier.weight(1f),
                    enabled = isFormValid
                ) {
                    Text(stringResource(R.string.save))
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { dateMillis = it }
                    showDatePicker = false
                }) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showNextDatePicker) {
        val nextDatePickerState = rememberDatePickerState(
            initialSelectedDateMillis = if (nextDateMillis > 0L) nextDateMillis else null
        )
        DatePickerDialog(
            onDismissRequest = { showNextDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    nextDateMillis = nextDatePickerState.selectedDateMillis ?: 0L
                    showNextDatePicker = false
                }) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showNextDatePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = nextDatePickerState)
        }
    }
}
