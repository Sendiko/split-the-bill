package com.sendiko.split_the_bill.helper

import java.text.NumberFormat
import java.util.Locale

fun isNotEmpty(list: List<*>?): Boolean {
    return list != null && list.isNotEmpty()
}

fun String.formatToRupiah(): String {
    val number = this.toDoubleOrNull()
    val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return if (number == null) "" else formatRupiah.format(number)
}