package com.savecar.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.savecar.app.data.entity.FuelConsumption

@Dao
interface FuelConsumptionDao {
    @Insert
    suspend fun insert(fuelConsumption: FuelConsumption): Long

    @Update
    suspend fun update(fuelConsumption: FuelConsumption)

    @Delete
    suspend fun delete(fuelConsumption: FuelConsumption)

    @Query("SELECT * FROM fuel_consumption WHERE id = :id")
    suspend fun getFuelConsumptionById(id: Int): FuelConsumption?

    @Query("SELECT * FROM fuel_consumption WHERE vehicleId = :vehicleId ORDER BY date DESC")
    suspend fun getFuelConsumptionByVehicleId(vehicleId: Int): List<FuelConsumption>

    @Query("SELECT * FROM fuel_consumption ORDER BY date DESC")
    suspend fun getAllFuelConsumption(): List<FuelConsumption>

    @Query("DELETE FROM fuel_consumption WHERE id = :id")
    suspend fun deleteFuelConsumptionById(id: Int)
}