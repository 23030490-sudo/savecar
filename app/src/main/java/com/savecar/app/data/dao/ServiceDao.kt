package com.savecar.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.savecar.app.data.entity.Service

@Dao
interface ServiceDao {
    @Insert
    suspend fun insert(service: Service): Long

    @Update
    suspend fun update(service: Service)

    @Delete
    suspend fun delete(service: Service)

    @Query("SELECT * FROM services WHERE id = :id")
    suspend fun getServiceById(id: Int): Service?

    @Query("SELECT * FROM services WHERE vehicleId = :vehicleId ORDER BY serviceDate DESC")
    suspend fun getServicesByVehicleId(vehicleId: Int): List<Service>

    @Query("SELECT * FROM services ORDER BY serviceDate DESC")
    suspend fun getAllServices(): List<Service>

    @Query("DELETE FROM services WHERE id = :id")
    suspend fun deleteServiceById(id: Int)
}