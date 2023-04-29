package com.sendiko.split_the_bill.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SplitBillViewModel : ViewModel() {

    private val _bill = MutableStateFlow("")
    val bill = _bill.asStateFlow()

    private val _person = MutableStateFlow("")
    val person = _person.asStateFlow()

    private val _splittedBill = MutableStateFlow("")
    val splittedBill = _splittedBill.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _emptyBill = MutableStateFlow(false)
    val emptyBill = _emptyBill.asStateFlow()

    private val _emptyPerson = MutableStateFlow(false)
    val emptyPerson = _emptyPerson.asStateFlow()

    fun setBill(bill: String) {
        _bill.value = bill
    }

    fun setPerson(person: String) {
        _person.value = person
    }

    fun clearState(){
        _bill.value = ""
        _person.value = ""
        _splittedBill.value = ""
    }

    fun splitTheBill(bill: String, person: String) {
        val newBill = bill.toIntOrNull()
        val newPerson = person.toIntOrNull()
        if (newBill != null) {
            if (newPerson != null) {
                when {
                    newBill == 0 -> {
                        _errorMessage.value = "the bill can't be 0"
                        _emptyBill.value = true
                    }

                    newPerson == 0 -> {
                        _errorMessage.value = "person can't be 0"
                        _emptyPerson.value = true
                    }

                    newBill < newPerson -> _errorMessage.value =
                        "the bill can't be less than the people"

                    newPerson == 1 -> _errorMessage.value = "you just pay for yourself dummy"
                    else -> {
                        _errorMessage.value = ""
                        _splittedBill.value = newBill.div(newPerson).toString()
                        _emptyPerson.value = false
                        _emptyBill.value = false
                    }
                }
            } else {
                _errorMessage.value = "person can't be null"
                _emptyPerson.value = true
                _emptyBill.value = false
            }
        } else {
            _emptyBill.value = true
            _errorMessage.value = "the bill can't be null"
        }
    }

}