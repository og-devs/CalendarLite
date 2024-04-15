package com.example.calendario.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDateTime
import java.time.ZonedDateTime


@Entity(tableName = "item")
@TypeConverters(Item.Converters::class)
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val startDate: LocalDateTime,
    val endTime: String?,
) {
    class Converters {
        @TypeConverter
        fun fromTimestamp(value: Long?): LocalDateTime? {
            return value?.let {
                LocalDateTime.ofEpochSecond(it, 0, ZonedDateTime.now().offset)
            }
        }

        @TypeConverter
        fun dateToTimestamp(date: LocalDateTime?): Long? {
            return date?.toEpochSecond(ZonedDateTime.now().offset)
        }
    }
}

