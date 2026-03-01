package com.savecar.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [/* Add your entity classes here */], version = 1, exportSchema = false)
abstract class SaveCarDatabase : RoomDatabase() {

    abstract fun carDao(): CarDao // Add your DAO interface here

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