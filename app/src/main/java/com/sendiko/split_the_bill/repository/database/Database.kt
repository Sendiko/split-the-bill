package com.sendiko.split_the_bill.repository.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sendiko.split_the_bill.repository.models.Bills

@androidx.room.Database(
    entities = [Bills::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class Database: RoomDatabase() {
    abstract val dao: BillDao

    companion object {
        @Volatile
        private var INSTANCE: Database? = null

        fun getDatabase(context: Context): Database {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "splitbill.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }

}