package com.savecar.app.data.repository

import com.savecar.app.data.local.dao.MaintenanceRecordDao
import com.savecar.app.data.local.dao.VehicleDao
import com.savecar.app.data.local.entity.MaintenanceRecord
import com.savecar.app.data.local.entity.Vehicle
import kotlinx.coroutines.flow.Flow

class SaveCarRepository(
    private val vehicleDao: VehicleDao,
    private val maintenanceRecordDao: MaintenanceRecordDao
) {

    // --- Vehicle operations ---

    fun getAllVehicles(): Flow<List<Vehicle>> = vehicleDao.getAllVehicles()

    fun getVehicleById(id: Long): Flow<Vehicle?> = vehicleDao.getVehicleById(id)

    suspend fun insertVehicle(vehicle: Vehicle): Long = vehicleDao.insertVehicle(vehicle)

    suspend fun updateVehicle(vehicle: Vehicle) = vehicleDao.updateVehicle(vehicle)

    suspend fun deleteVehicle(vehicle: Vehicle) = vehicleDao.deleteVehicle(vehicle)

    // --- Maintenance record operations ---

    fun getRecordsForVehicle(vehicleId: Long): Flow<List<MaintenanceRecord>> =
        maintenanceRecordDao.getRecordsForVehicle(vehicleId)

    fun getRecordById(id: Long): Flow<MaintenanceRecord?> =
        maintenanceRecordDao.getRecordById(id)

    fun getRecentRecords(limit: Int = 5): Flow<List<MaintenanceRecord>> =
        maintenanceRecordDao.getRecentRecords(limit)

    suspend fun insertRecord(record: MaintenanceRecord): Long =
        maintenanceRecordDao.insertRecord(record)

    suspend fun updateRecord(record: MaintenanceRecord) =
        maintenanceRecordDao.updateRecord(record)

    suspend fun deleteRecord(record: MaintenanceRecord) =
        maintenanceRecordDao.deleteRecord(record)
}
