package com.savecar.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "maintenance_records",
    foreignKeys = [
        ForeignKey(
            entity = Vehicle::class,
            parentColumns = ["id"],
            childColumns = ["vehicleId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("vehicleId")]
)
data class MaintenanceRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val vehicleId: Long,
    val type: String,
    val date: Long,
    val mileageAtService: Int,
    val cost: Double = 0.0,
    val workshop: String = "",
    val notes: String = "",
    val nextServiceMileage: Int = 0,
    val nextServiceDate: Long = 0L
)
