package com.sendiko.split_the_bill.ui.events

import com.sendiko.split_the_bill.repository.models.Bills

sealed interface SplitBillEvent {
    object ClearState : SplitBillEvent
    object SaveSplitBill : SplitBillEvent
    object ExceedMaxDigits: SplitBillEvent
    data class DeleteSplitBill(val bills: Bills) : SplitBillEvent
    data class SetBill(val bill: String) : SplitBillEvent
    data class SetPerson(val person: String) : SplitBillEvent
    data class SetSplittedSplitBill(val splittedBill: String) : SplitBillEvent
}