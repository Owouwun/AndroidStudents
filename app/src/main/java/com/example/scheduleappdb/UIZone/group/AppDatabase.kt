package com.example.scheduleappdb.UIZone.group

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ GroupOperator::class ], version=1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun groupOperatorDao(): GroupOperatorDao
}