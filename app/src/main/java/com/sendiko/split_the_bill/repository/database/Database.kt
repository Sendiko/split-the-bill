package com.sendiko.split_the_bill.repository.database

import androidx.room.AutoMigration
import androidx.room.RoomDatabase
import com.sendiko.split_the_bill.repository.models.Bills

@androidx.room.Database(
    entities = [Bills::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class Database: RoomDatabase() {
    abstract val dao: BillDao
}