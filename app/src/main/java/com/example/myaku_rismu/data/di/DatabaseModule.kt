package com.example.myaku_rismu.data.di

import android.content.Context
import androidx.room.Room
import com.example.myaku_rismu.data.database.database.AppDatabase
import com.example.myaku_rismu.data.database.dao.MusicGenerationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
        .build()
    }
    
    @Provides
    fun provideMusicGenerationDao(database: AppDatabase): MusicGenerationDao {
        return database.musicGenerationDao()
    }
}