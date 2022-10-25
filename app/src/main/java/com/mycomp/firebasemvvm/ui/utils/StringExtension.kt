package com.mycomp.firebasemvvm.ui.utils

fun String.kbToMb(): String {
    val number = (this.toDouble() / (1024 * 1024))
    val number3digits: Double = String.format("%.3f", number).toDouble()
    return number3digits.toString()
}


