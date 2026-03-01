package com.savecar.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.savecar.app.data.local.entity.MaintenanceRecord
import com.savecar.app.data.local.entity.Vehicle
import com.savecar.app.data.repository.SaveCarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SaveCarViewModel(private val repository: SaveCarRepository) : ViewModel() {

    val vehicles: StateFlow<List<Vehicle>> = repository.getAllVehicles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val recentRecords: StateFlow<List<MaintenanceRecord>> = repository.getRecentRecords(10)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _selectedVehicleId = MutableStateFlow<Long?>(null)

    val selectedVehicle: StateFlow<Vehicle?> = _selectedVehicleId
        .flatMapLatest { id -> if (id != null) repository.getVehicleById(id) else flowOf(null) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val vehicleRecords: StateFlow<List<MaintenanceRecord>> = _selectedVehicleId
        .flatMapLatest { id ->
            if (id != null) repository.getRecordsForVehicle(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun selectVehicle(vehicleId: Long) {
        _selectedVehicleId.value = vehicleId
    }

    fun addVehicle(vehicle: Vehicle, onSuccess: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repository.insertVehicle(vehicle)
            onSuccess(id)
        }
    }

    fun updateVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            repository.updateVehicle(vehicle)
        }
    }

    fun deleteVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            repository.deleteVehicle(vehicle)
        }
    }

    fun addMaintenanceRecord(record: MaintenanceRecord) {
        viewModelScope.launch {
            repository.insertRecord(record)
        }
    }

    fun updateMaintenanceRecord(record: MaintenanceRecord) {
        viewModelScope.launch {
            repository.updateRecord(record)
        }
    }

    fun deleteMaintenanceRecord(record: MaintenanceRecord) {
        viewModelScope.launch {
            repository.deleteRecord(record)
        }
    }

    class Factory(private val repository: SaveCarRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SaveCarViewModel::class.java)) {
                return SaveCarViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
