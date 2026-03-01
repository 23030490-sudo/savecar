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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.savecar.app.data.local.entity.Vehicle

private val FUEL_TYPES = listOf("Gasolina", "Diésel", "Híbrido", "Eléctrico", "GLP", "GNC")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditVehicleScreen(
    vehicle: Vehicle?,
    onSave: (Vehicle) -> Unit,
    onCancel: () -> Unit
) {
    var brand by rememberSaveable { mutableStateOf(vehicle?.brand ?: "") }
    var model by rememberSaveable { mutableStateOf(vehicle?.model ?: "") }
    var year by rememberSaveable { mutableStateOf(vehicle?.year?.toString() ?: "") }
    var licensePlate by rememberSaveable { mutableStateOf(vehicle?.licensePlate ?: "") }
    var color by rememberSaveable { mutableStateOf(vehicle?.color ?: "") }
    var mileage by rememberSaveable { mutableStateOf(vehicle?.mileage?.toString() ?: "") }
    var fuelType by rememberSaveable { mutableStateOf(vehicle?.fuelType ?: FUEL_TYPES[0]) }
    var notes by rememberSaveable { mutableStateOf(vehicle?.notes ?: "") }
    var fuelDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    val isEditing = vehicle != null
    val title = if (isEditing) stringResource(R.string.edit_vehicle)
                else stringResource(R.string.add_vehicle)

    val isFormValid = brand.isNotBlank() && model.isNotBlank() &&
        year.isNotBlank() && licensePlate.isNotBlank()

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
            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text(stringResource(R.string.brand)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                label = { Text(stringResource(R.string.model)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = year,
                    onValueChange = { if (it.length <= 4) year = it },
                    label = { Text(stringResource(R.string.year)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = licensePlate,
                    onValueChange = { licensePlate = it.uppercase() },
                    label = { Text(stringResource(R.string.license_plate)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = color,
                    onValueChange = { color = it },
                    label = { Text(stringResource(R.string.color)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = mileage,
                    onValueChange = { mileage = it },
                    label = { Text(stringResource(R.string.mileage_km)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            ExposedDropdownMenuBox(
                expanded = fuelDropdownExpanded,
                onExpandedChange = { fuelDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = fuelType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.fuel_type)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fuelDropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = fuelDropdownExpanded,
                    onDismissRequest = { fuelDropdownExpanded = false }
                ) {
                    FUEL_TYPES.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                fuelType = type
                                fuelDropdownExpanded = false
                            }
                        )
                    }
                }
            }
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text(stringResource(R.string.notes)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )
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
                        val v = Vehicle(
                            id = vehicle?.id ?: 0L,
                            brand = brand.trim(),
                            model = model.trim(),
                            year = year.toIntOrNull() ?: 0,
                            licensePlate = licensePlate.trim(),
                            color = color.trim(),
                            mileage = mileage.toIntOrNull() ?: 0,
                            fuelType = fuelType,
                            notes = notes.trim()
                        )
                        onSave(v)
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
}
