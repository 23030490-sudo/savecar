package com.savecar.app

import android.app.Application
import com.savecar.app.data.local.SaveCarDatabase
import com.savecar.app.data.repository.SaveCarRepository

class SaveCarApplication : Application() {

    val database by lazy { SaveCarDatabase.getDatabase(this) }
    val repository by lazy {
        SaveCarRepository(
            vehicleDao = database.vehicleDao(),
            maintenanceRecordDao = database.maintenanceRecordDao()
        )
    }
}
