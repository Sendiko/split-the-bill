package com.sendiko.split_the_bill.repository.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sendiko.split_the_bill.repository.database.Database
import com.sendiko.split_the_bill.repository.models.Bills
import com.sendiko.split_the_bill.ui.events.SplitBillEvent
import com.sendiko.split_the_bill.ui.state.SplitBillState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplitBillViewModel(
    app: Application
) : ViewModel() {

    private val dao = Database.getDatabase(app.applicationContext).dao

    private val _state = MutableStateFlow(SplitBillState())
    private val _bills = dao.getBills()
    val state = combine(_state, _bills){ state, bills ->
        state.copy(
            bills = bills
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SplitBillState())

    fun onEvent(event: SplitBillEvent) {
        when (event) {
            SplitBillEvent.ClearState -> _state.update {
                it.copy(
                    bill = "",
                    person = "",
                    finalBill = "",
                    errorMessage = "",
                    errorBillInput = false,
                    errorPersonInput = false,
                    isStupid = false,
                    isShowingSuccessMessage = false
                )
            }
            SplitBillEvent.SaveSplitBill -> viewModelScope.launch {
                val bill = state.value.bill.toIntOrNull()
                val person = state.value.person.toIntOrNull()
                if (bill == null){
                    _state.update { it.copy(
                        errorMessage = "the bill can't be null",
                        errorBillInput = true
                    ) }
                    return@launch
                }
                if (person == null) {
                    _state.update { it.copy(
                        errorMessage = "person can't be null",
                        errorPersonInput = true,
                        errorBillInput = false
                    ) }
                    return@launch
                }
                if(person == 1){
                    _state.update { it.copy(
                        isStupid = true,
                        errorMessage = "you just pay full price dummy"
                    ) }
                    return@launch
                }
                if (person > bill){
                    _state.update { it.copy(
                        errorMessage = "bill can't be less than people",
                        errorPersonInput = true,
                        errorBillInput = false
                    ) }
                    return@launch
                }
                _state.update { it.copy(
                    finalBill = bill.div(person).toString(),
                    errorMessage = "",
                    errorBillInput = false,
                    errorPersonInput = false,
                    isShowingSuccessMessage = true
                ) }
                dao.insertBill(Bills(bill = bill.toString(), person = person.toString(), splittedBill = bill.div(person).toString()))
            }
            SplitBillEvent.ExceedMaxDigits -> {
                _state.update { it.copy(
                    errorBillInput = true,
                    errorMessage = "max character exceeded"
                ) }
            }
            SplitBillEvent.ClearMessageState -> _state.update { it.copy(
                isStupid = false,
                isShowingSuccessMessage = false
            ) }
            is SplitBillEvent.DeleteSplitBill -> viewModelScope.launch {
                dao.deleteBill(event.bills)
            }
            is SplitBillEvent.SetPerson -> _state.update { it.copy(person = event.person) }
            is SplitBillEvent.SetBill -> _state.update { it.copy(bill = event.bill) }
            is SplitBillEvent.SetSplittedSplitBill -> _state.update { it.copy(finalBill = event.splittedBill) }
        }
    }

}