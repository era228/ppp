package com.example.ppp.model

import com.google.gson.annotations.SerializedName

data class CbrResult(
    @SerializedName("Valute")
    val valute: HashMap<String, Valuta>
) {
    class Valuta(
        @SerializedName("Value")
        val value: String
    )
}
