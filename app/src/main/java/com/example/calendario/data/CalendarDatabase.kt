package com.example.calendario.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Item::class],
    version = 1,
    exportSchema = false
)
abstract class CalendarDatabase : RoomDatabase() {

    abstract val itemDao: ItemDao

}