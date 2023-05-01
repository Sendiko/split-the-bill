package com.sendiko.split_the_bill.repository.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bills")
data class Bills(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val bill: String,
    val person: String,
    val splittedBill: String,
)
