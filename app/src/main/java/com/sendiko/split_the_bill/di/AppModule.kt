package com.sendiko.split_the_bill.di

import android.content.Context
import androidx.room.Room
import com.sendiko.split_the_bill.dataStore
import com.sendiko.split_the_bill.repository.AppPreferences
import com.sendiko.split_the_bill.repository.database.BillDao
import com.sendiko.split_the_bill.repository.database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(
            context = context,
            klass = Database::class.java,
            name = "splitbill.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideDao(database: Database): BillDao{
        return database.dao
    }

    @Singleton
    @Provides
    fun providePreferences(@ApplicationContext context: Context): AppPreferences{
        val dataStore = context.dataStore
        return AppPreferences(dataStore)
    }
}