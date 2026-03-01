package com.savecar.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val brand: String,
    val model: String,
    val year: Int,
    val licensePlate: String,
    val mileage: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)