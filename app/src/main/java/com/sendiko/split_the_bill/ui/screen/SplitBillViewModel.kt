package com.sendiko.split_the_bill.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sendiko.split_the_bill.repository.AppPreferences
import com.sendiko.split_the_bill.repository.database.BillDao
import com.sendiko.split_the_bill.repository.models.Bills
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class SplitBillViewModel @Inject constructor(
    private val dao: BillDao,
    private val pref: AppPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(SplitBillState())
    private val _bills = dao.getBills()
    private val _isDarkTheme = pref.getDarkTheme()
    val state = combine(_state, _bills, _isDarkTheme) { state, bills, isDarkTheme ->
        state.copy(
            bills = bills,
            isDarkTheme = isDarkTheme
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SplitBillState())

    @RequiresApi(Build.VERSION_CODES.O)
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
                val bill = state.value.bill.deleteCommaAndPeriod().toDoubleOrNull()
                val person = state.value.person.toIntOrNull()
                if (bill == null) {
                    _state.update {
                        it.copy(
                            errorMessage = "the bill can't be null",
                            errorBillInput = true
                        )
                    }
                    return@launch
                }
                if (person == null) {
                    _state.update {
                        it.copy(
                            errorMessage = "person can't be null",
                            errorPersonInput = true,
                            errorBillInput = false
                        )
                    }
                    return@launch
                }
                if (person == 1) {
                    _state.update {
                        it.copy(
                            isStupid = true,
                            errorMessage = "you just pay full price dummy"
                        )
                    }
                    return@launch
                }
                if (person > bill) {
                    _state.update {
                        it.copy(
                            errorMessage = "bill can't be less than people",
                            errorPersonInput = true,
                            errorBillInput = false
                        )
                    }
                    return@launch
                }
                _state.update {
                    it.copy(
                        finalBill = bill.div(person).toString(),
                        errorMessage = "",
                        errorBillInput = false,
                        errorPersonInput = false,
                        isShowingSuccessMessage = true
                    )
                }
                val date = LocalDate.parse(LocalDate.now().toString())
                val formattedDate = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                dao.insertBill(
                    Bills(
                        bill = bill.toString(),
                        person = person.toString(),
                        splittedBill = bill.div(person).toString(),
                        date = formattedDate
                    )
                )
            }

            SplitBillEvent.ExceedMaxDigits -> {
                _state.update {
                    it.copy(
                        errorBillInput = true,
                        errorMessage = "max character exceeded"
                    )
                }
            }

            SplitBillEvent.ClearMessageState -> _state.update {
                it.copy(
                    isStupid = false,
                    isShowingSuccessMessage = false
                )
            }

            is SplitBillEvent.DeleteSplitBill -> viewModelScope.launch {
                dao.deleteBill(event.bills)
            }

            is SplitBillEvent.SetPerson -> _state.update { it.copy(person = event.person) }
            is SplitBillEvent.SetBill -> _state.update { it.copy(bill = event.bill) }
            is SplitBillEvent.SetSplittedSplitBill -> _state.update { it.copy(finalBill = event.splittedBill) }
            is SplitBillEvent.SetShowDialog -> _state.update { it.copy(dialog = event.dialog) }
            is SplitBillEvent.SetShowDropDown -> _state.update { it.copy(isShowingDropDown = event.isShowing) }
            is SplitBillEvent.SetDarkTheme -> viewModelScope.launch {
                pref.setDarkTheme(event.isDark)
            }
        }
    }

    private fun String.deleteCommaAndPeriod(): String{
        return replace(".", "").replace(",", "")
    }

}