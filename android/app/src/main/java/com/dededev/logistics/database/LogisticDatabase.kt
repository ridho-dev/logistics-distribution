package com.dededev.logistics.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Logistic::class], version = 1)
abstract class LogisticDatabase: RoomDatabase() {
    abstract fun logisticDao(): LogisticDao

    companion object {
        @Volatile
        private var INSTANCE: LogisticDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): LogisticDatabase {
            if (INSTANCE == null) {
                synchronized(LogisticDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        LogisticDatabase::class.java, "logistic_database")
                        .build()
                }
            }
            return INSTANCE as LogisticDatabase
        }
    }
}