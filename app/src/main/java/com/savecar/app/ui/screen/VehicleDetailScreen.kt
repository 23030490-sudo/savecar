package com.savecar.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.savecar.app.R
import com.savecar.app.data.local.entity.MaintenanceRecord
import com.savecar.app.data.local.entity.Vehicle
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailScreen(
    vehicle: Vehicle?,
    maintenanceRecords: List<MaintenanceRecord>,
    onEditVehicle: () -> Unit,
    onDeleteVehicle: (Vehicle) -> Unit,
    onAddRecord: () -> Unit,
    onEditRecord: (MaintenanceRecord) -> Unit,
    onDeleteRecord: (MaintenanceRecord) -> Unit,
    onNavigateBack: () -> Unit
) {
    var showDeleteVehicleDialog by remember { mutableStateOf(false) }
    var recordToDelete by remember { mutableStateOf<MaintenanceRecord?>(null) }

    if (vehicle == null) return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${vehicle.brand} ${vehicle.model}") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onEditVehicle) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit_vehicle)
                        )
                    }
                    IconButton(onClick = { showDeleteVehicleDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete_vehicle)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddRecord,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_maintenance),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                VehicleInfoCard(vehicle = vehicle)
            }

            item {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.maintenance_history),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            if (maintenanceRecords.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.no_records_message),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(maintenanceRecords, key = { it.id }) { record ->
                    MaintenanceRecordCard(
                        record = record,
                        onEdit = { onEditRecord(record) },
                        onDelete = { recordToDelete = record }
                    )
                }
            }
        }
    }

    if (showDeleteVehicleDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteVehicleDialog = false },
            title = { Text(stringResource(R.string.delete_vehicle)) },
            text = { Text(stringResource(R.string.delete_vehicle_confirm, vehicle.brand, vehicle.model)) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteVehicleDialog = false
                    onDeleteVehicle(vehicle)
                }) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteVehicleDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    recordToDelete?.let { record ->
        AlertDialog(
            onDismissRequest = { recordToDelete = null },
            title = { Text(stringResource(R.string.delete_record)) },
            text = { Text(stringResource(R.string.delete_record_confirm, record.type)) },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteRecord(record)
                    recordToDelete = null
                }) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { recordToDelete = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun VehicleInfoCard(vehicle: Vehicle) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = "${vehicle.brand} ${vehicle.model}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = vehicle.year.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            VehicleInfoRow(label = stringResource(R.string.license_plate), value = vehicle.licensePlate)
            if (vehicle.color.isNotBlank()) {
                VehicleInfoRow(label = stringResource(R.string.color), value = vehicle.color)
            }
            VehicleInfoRow(label = stringResource(R.string.fuel_type), value = vehicle.fuelType)
            if (vehicle.mileage > 0) {
                VehicleInfoRow(label = stringResource(R.string.mileage_km), value = "${vehicle.mileage} km")
            }
            if (vehicle.notes.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = vehicle.notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun VehicleInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun MaintenanceRecordCard(
    record: MaintenanceRecord,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = record.type,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = dateFormat.format(Date(record.date)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit_record),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete_record),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
            if (record.mileageAtService > 0) {
                Text(
                    text = "${stringResource(R.string.mileage_km)}: ${record.mileageAtService} km",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (record.cost > 0) {
                Text(
                    text = "${stringResource(R.string.cost)}: ${currencyFormat.format(record.cost)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (record.workshop.isNotBlank()) {
                Text(
                    text = "${stringResource(R.string.workshop)}: ${record.workshop}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (record.notes.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = record.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (record.nextServiceMileage > 0 || record.nextServiceDate > 0L) {
                Spacer(Modifier.height(4.dp))
                val nextText = buildString {
                    append(stringResource(R.string.next_service))
                    append(": ")
                    if (record.nextServiceMileage > 0) append("${record.nextServiceMileage} km")
                    if (record.nextServiceMileage > 0 && record.nextServiceDate > 0L) append(" · ")
                    if (record.nextServiceDate > 0L) append(dateFormat.format(Date(record.nextServiceDate)))
                }
                Text(
                    text = nextText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
