package com.savecar.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.savecar.app.data.local.dao.MaintenanceRecordDao
import com.savecar.app.data.local.dao.VehicleDao
import com.savecar.app.data.local.entity.MaintenanceRecord
import com.savecar.app.data.local.entity.Vehicle

@Database(
    entities = [Vehicle::class, MaintenanceRecord::class],
    version = 1,
    exportSchema = false
)
abstract class SaveCarDatabase : RoomDatabase() {

    abstract fun vehicleDao(): VehicleDao
    abstract fun maintenanceRecordDao(): MaintenanceRecordDao

    companion object {
        @Volatile
        private var INSTANCE: SaveCarDatabase? = null

        fun getDatabase(context: Context): SaveCarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SaveCarDatabase::class.java,
                    "savecar_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
