package com.example.ppp

import java.text.DecimalFormat

object Utils {
    fun formatAmount(double: Double): String {
        return DecimalFormat("$0.00").format(double)
    }
}