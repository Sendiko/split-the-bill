package com.sendiko.split_the_bill.ui.state

import com.sendiko.split_the_bill.repository.models.Bills

data class SplitBillState(
    val bills: List<Bills> = emptyList(),
    val bill: String = "",
    val person: String = "",
    val finalBill: String = "",
    val errorMessage: String = "",
    val errorBillInput: Boolean = false,
    val errorPersonInput: Boolean = false,
    val maxBillDigits: Int = 10,
    val isStupid: Boolean = false,
    val isShowingSuccessMessage: Boolean = false,
)
