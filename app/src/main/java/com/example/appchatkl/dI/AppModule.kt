package com.example.appchatkl.dI

import android.app.Application
import com.example.appchatkl.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDatabase(application: Application) = AppDatabase.getDatabase(application)

    @Singleton
    @Provides
    fun provideNoteDao(database: AppDatabase) = database.chatDao()
}