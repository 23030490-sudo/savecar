package com.savecar.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val brand: String,
    val model: String,
    val year: Int,
    val licensePlate: String,
    val color: String = "",
    val mileage: Int = 0,
    val fuelType: String = "Gasolina",
    val notes: String = ""
)
