package com.savecar.app

import android.app.Application
import androidx.room.Room
import com.savecar.app.data.database.SaveCarDatabase

class SaveCarApplication : Application() {
    companion object {
        lateinit var database: SaveCarDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        
        // Inicializar Room Database
        database = Room.databaseBuilder(
            this,
            SaveCarDatabase::class.java,
            "savecar_db"
        ).build()
    }
}