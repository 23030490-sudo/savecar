package com.savecar.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "services",
    foreignKeys = [
        ForeignKey(
            entity = Vehicle::class,
            parentColumns = ["id"],
            childColumns = ["vehicleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
) 
data class Service(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val vehicleId: Int,
    val serviceType: String,
    val description: String,
    val cost: Double,
    val mileage: Int,
    val serviceDate: Long,
    val nextServiceDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)