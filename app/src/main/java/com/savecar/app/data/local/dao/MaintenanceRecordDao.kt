package com.savecar.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.savecar.app.data.local.entity.MaintenanceRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceRecordDao {

    @Query("SELECT * FROM maintenance_records WHERE vehicleId = :vehicleId ORDER BY date DESC")
    fun getRecordsForVehicle(vehicleId: Long): Flow<List<MaintenanceRecord>>

    @Query("SELECT * FROM maintenance_records WHERE id = :id")
    fun getRecordById(id: Long): Flow<MaintenanceRecord?>

    @Query("SELECT * FROM maintenance_records ORDER BY date DESC LIMIT :limit")
    fun getRecentRecords(limit: Int = 5): Flow<List<MaintenanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: MaintenanceRecord): Long

    @Update
    suspend fun updateRecord(record: MaintenanceRecord)

    @Delete
    suspend fun deleteRecord(record: MaintenanceRecord)
}
