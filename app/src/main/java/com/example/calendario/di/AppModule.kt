package com.example.calendario.di

import android.app.Application
import androidx.room.Room
import com.example.calendario.data.CalendarDatabase
import com.example.calendario.data.ItemsRepository
import com.example.calendario.data.ItemsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCalendarDatabase(application: Application): CalendarDatabase {
        return Room.databaseBuilder(
            application,
            CalendarDatabase::class.java,
            "calendar_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCalendarRepository(database: CalendarDatabase): ItemsRepository {
        return ItemsRepositoryImpl(database.itemDao)
    }
}