package com.sendiko.split_the_bill.repository.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sendiko.split_the_bill.repository.models.Bills
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bills: Bills)

    @Delete
    suspend fun deleteBill(bills: Bills)

    @Query("select * from bills order by id desc")
    fun getBills(): Flow<List<Bills>>
}